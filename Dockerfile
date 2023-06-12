FROM gradle:jdk17-alpine
ADD / /
WORKDIR /
RUN gradle shadow
EXPOSE 80
ENV PORT=80
CMD sh -c  "java -jar /build/libs/NewBing-web-1.0-SNAPSHOT-all.jar \$PORT"