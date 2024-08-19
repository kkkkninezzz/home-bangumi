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
