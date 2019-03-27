FROM maven:3.6.0-jdk-8-alpine as builder
COPY  . /root/app/
WORKDIR /root/app
RUN mvn install -DskipTests

FROM oracle/graalvm-ce:1.0.0-rc11 as graalvm
COPY --from=builder /root/app/ /home/app/
WORKDIR /home/app
RUN java -cp target/model-quotes-0.1.jar \
            io.micronaut.graal.reflect.GraalClassLoadingAnalyzer \
            reflect.json
RUN native-image --no-server \
                 --class-path target/model-quotes-0.1.jar \
                 -H:ReflectionConfigurationFiles=/home/app/reflect.json \
                 -H:EnableURLProtocols=http \
                 -H:IncludeResources='logback.xml|application.yml|META-INF/services/*.*' \
                 -H:+ReportUnsupportedElementsAtRuntime \
                 -H:+AllowVMInspection \
                 --rerun-class-initialization-at-runtime='sun.security.jca.JCAUtil$CachedSecureRandomHolder',javax.net.ssl.SSLContext \
                 --delay-class-initialization-to-runtime=io.netty.handler.codec.http.HttpObjectEncoder,io.netty.handler.codec.http.websocketx.WebSocket00FrameEncoder,io.netty.handler.ssl.util.ThreadLocalInsecureRandom,com.sun.jndi.dns.DnsClient \
                 -H:-UseServiceLoaderFeature \
                 --allow-incomplete-classpath \
                 -H:Name=model-quotes \
                 -H:Class=model.quotes.Application


FROM frolvlad/alpine-glibc
EXPOSE 8080
COPY --from=graalvm /home/app/model-quotes .
ENTRYPOINT ["./model-quotes"]
