FROM adoptopenjdk/openjdk11:alpine-jre
ADD build/libs/navi-0.1.jar navi.jar
ENTRYPOINT [ "java", "-jar", "navi.jar" ]