export default {
  path: "/system-settings",
  redirect: "/system-settings/network-proxy/index",
  meta: {
    icon: "ant-design:setting-filled",
    title: "系统设置"
  },
  children: [
    {
      path: "/system-settings/network-proxy/index",
      name: "NetworkProxySettings",
      component: () => import("@/views/systemsettings/networkproxy/index.vue"),
      meta: {
        icon: "icon-park-outline:database-network-point",
        title: "网络代理"
      }
    },
    {
      path: "/system-settings/downloader/index",
      redirect: "/system-settings/downloader/qbittorrent",
      name: "DownloaderSettings",
      meta: {
        icon: "mdi:cloud-download",
        title: "下载器"
      },
      children: [
        {
          path: "/system-settings/downloader/qbittorrent",
          component: () =>
            import(
              "@/views/systemsettings/downloader/components/QbittorrentDownloaderSettingsPage.vue"
            ),
          name: "QbittorrentDownloaderSettingsPage",
          meta: {
            title: "qbittorrent",
            icon: "cbi:qbittorrent",

            // 通过设置showParent为true，显示父级
            showParent: true
          }
        }
      ]
    },
    {
      path: "/system-settings/filter-rules/index",
      name: "FilterRulesSettings",
      component: () => import("@/views/systemsettings/filterrules/index.vue"),
      meta: {
        icon: "material-symbols:filter-alt",
        title: "过滤规则"
      }
    },
    {
      path: "/system-settings/scheduled-task/index",
      name: "ScheduledtaskSettings",
      component: () => import("@/views/systemsettings/scheduledtask/index.vue"),
      meta: {
        icon: "gridicons:scheduled",
        title: "定时任务"
      }
    },
    {
      path: "/system-settings/message-pusher/index",
      redirect: "/system-settings/message-pusher/wecomchan",
      name: "MessagePusherSettings",
      meta: {
        icon: "ant-design:message-filled",
        title: "消息推送"
      },
      children: [
        {
          path: "/system-settings/message-pusher/wecomchan",
          component: () =>
            import(
              "@/views/systemsettings/messagepusher/components/WecomchanSettingsPage.vue"
            ),
          name: "WecomchanSettingsPage",
          meta: {
            title: "wecomchan",
            icon: "ant-design:wechat-filled",

            // 通过设置showParent为true，显示父级
            showParent: true
          }
        }
      ]
    },
    {
      path: "/system-settings/rename-task/index",
      redirect: "/system-settings/rename-task/episode",
      name: "RenameTaskSettings",
      meta: {
        icon: "fluent:task-list-square-settings-20-regular",
        title: "重命名任务"
      },
      children: [
        {
          path: "/system-settings/rename-task/episode",
          component: () =>
            import(
              "@/views/systemsettings/renametask/components/EpisodeRenameTaskSettingsPage.vue"
            ),
          name: "EpisodeRenameTaskSettingsPage",
          meta: {
            title: "剧集重命名",
            icon: "mdi:rename-outline",

            // 通过设置showParent为true，显示父级
            showParent: true
          }
        }
      ]
    }
    // {
    //   path: "/system-settings/scraped-pase/index",
    //   name: "ScrapedPaseSettings",
    //   component: () => import("@/views/systemsettings/scrapedpase/index.vue"),
    //   meta: {
    //     icon: "material-symbols:360-rounded",
    //     title: "刮削解析"
    //   }
    // },
  ]
};
