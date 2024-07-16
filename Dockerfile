FROM ubuntu:jammy

RUN mkdir -p /opt/app && mkdir -p /usr/local/var/www/web-ui

COPY ./server/home-bangumi/home-bangumi-web/src/main/resources/application-prod.yaml /opt/app/application.yaml
COPY ./server/home-bangumi/out/home-bangumi-web /opt/app/home-bangumi-web
COPY ./web-ui/home-bangumi-web-ui/dist/ /usr/local/var/www/web-ui

# 对外开放的端口
EXPOSE 80

# 声明数据的挂载点
VOLUME /data/home-bangumi
VOLUME /logs

ENV TZ=Asia/Shanghai

# 复制启动脚本到容器中
COPY ./docker-entrypoint.sh /entrypoint.sh
# 添加可执行权限
RUN chmod +x /entrypoint.sh
# 设置ENTRYPOINT指令
ENTRYPOINT ["/entrypoint.sh"]
