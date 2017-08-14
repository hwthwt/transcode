FROM docker.io/alpine:latest
MAINTAINER dick_zhm dick_zhm@163.com
RUN apk --update add nginx
RUN apk --update add nginx-mod-rtmp
EXPOSE 80
CMD ["nginx","-g","daemon off;"]
