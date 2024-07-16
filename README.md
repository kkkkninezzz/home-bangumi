# home-bangumi
灵感来自于[Auto_Bangumi](https://github.com/EstrellaXD/Auto_Bangumi)。但是AB过于auto，对我来说无法方便去人工处理某些环节的问题，例如剧集过滤时，无法知道哪些剧集会被过滤。因此在长时间使用AB后，萌生了按照AB来实现一套自己的追番工具。

`Home Bangumi`是基于 RSS 的自动追番整理下载工具。基于[Mikan Project](https://mikanani.me/)的rss链接即可实现自动追番。

## 使用方式
仅推荐使用docker进行部署，通常建议选择最新的版本号使用，`latest`作为日常合并到master时就会更新，并不一定同步产生新的版本号。

在启动`rainine/home-bangumi`容器时，可以指定数据和日志的挂载目录，以确保数据和日志的持久化。以下是一个示例命令，展示了如何运行该容器并指定挂载目录和端口：

```bash
docker run -d \
  --name home-bangumi \
  -v /data/home-bangumi:/data/home-bangumi \
  -v /logs:/logs \
  -p 80:80 \
  rainine/home-bangumi
```

### 详细说明
#### 挂载目录：
* `/data/home-bangumi`：挂载到容器内的`/data/home-bangumi`目录，用于存储持久化数据。
* `/logs`：挂载到容器内的`/logs`目录，用于存储应用程序的日志文件。

#### 端口映射：
容器内的 80 端口映射到主机的 80 端口，用户可以通过`http://<主机IP或域名>` 访问 web 界面。

#### 账号登录:
目前并未实现密码修改，在第一次启动项目时，会生成默认账号`admin`，密码为`admin123`。web-ui里也默认设置了密码，所以直接登录即可。

## 鸣谢
* [pure-admin](https://github.com/pure-admin/vue-pure-admin)
* [plus-pro-components](https://github.com/plus-pro-components/plus-pro-components)
* [Sa-Token](https://github.com/dromara/Sa-Token)

## Licence
[MIT licence](https://github.com/kkkkninezzz/home-bangumi/blob/main/LICENSE)
