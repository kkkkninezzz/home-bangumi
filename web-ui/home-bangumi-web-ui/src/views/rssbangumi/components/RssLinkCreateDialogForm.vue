<script setup lang="ts">
import { ref, onBeforeMount, computed } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusDialogForm
} from "plus-pro-components";

import {
  RssBangumiPreviewReq,
  RssBangumiPreviewResp,
  previewRssBangumi
} from "@/api/rssBangumi";

import {
  EpisodeFilterRulesSettingsResp,
  getEpisodeFilterRulesSettings
} from "@/api/systemSettings";

import { message } from "@/utils/message";

import {
  RssBangumRssCategoryOptions,
  RssBangumiHandleMethodOptions,
  DownloaderCategoryEnum,
  DownloaderCategoryOptions,
  RssCategoryEnum,
  RssBangumiHandleMethodEnum,
  EpisodeTitleRenameMethodEnum,
  EpisodeTitleRenameMethodOptions
} from "../enums";

defineOptions({
  name: "RssLinkCreateDialogForm"
});

const visible = ref(true);
const previewLoading = ref(false);
const temporaryStorageLoading = ref(false);

// 关闭表单时通知父组件
const emit = defineEmits<{
  (e: "close-form"): void;
  (e: "preview-success", id: number): void;
}>();

// 提交rss链接相关的表单
const rssLinkState = ref<FieldValues>({
  rssCategory: RssCategoryEnum.Mikan, // rss链接类型 默认为mikan
  handleMethod: RssBangumiHandleMethodEnum.SUBSCRIBE, // 处理方式  默认为订阅
  rssName: "", // 链接名
  rssLink: "", // 实际链接
  skippedEpisodeNo: 0, // 跳过的剧集号
  filterRules: [], // 过滤规则
  downloaderCategory: DownloaderCategoryEnum.QBITTORRENT, // 下载器分类
  episodeTitleRenameMethod: EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE, // 剧集解析方式
  customizeRenamedEpisodeTitleFormat: "" // 自定义的重命名后标题格式
});

/** 初始化rss链接的表单 */
function initRssLinkState() {
  rssLinkState.value.rssCategory = RssCategoryEnum.Mikan;
  rssLinkState.value.handleMethod = RssBangumiHandleMethodEnum.SUBSCRIBE;
  rssLinkState.value.rssName = "";
  rssLinkState.value.rssLink = "";
  rssLinkState.value.skippedEpisodeNo = 0;
  //rssLinkState.value.filterRules = [];
  rssLinkState.value.downloaderCategory = DownloaderCategoryEnum.QBITTORRENT;
  rssLinkState.value.episodeTitleRenameMethod =
    EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE;
  rssLinkState.value.customizeRenamedEpisodeTitleFormat = "";
  setDefaultFilterRules();
}

async function setDefaultFilterRules() {
  const resp: EpisodeFilterRulesSettingsResp =
    await getEpisodeFilterRulesSettings();
  if (!resp.success) {
    return;
  }

  rssLinkState.value.filterRules = resp.data.rules ?? [];
}

onBeforeMount(() => {
  initRssLinkState();
});

// rss链接相关规则
const rssLinkRules = {
  rssCategory: [
    {
      required: true,
      message: "请选择rss链接类别"
    }
  ],
  handleMethod: [
    {
      required: true,
      message: "请选择处理方式"
    }
  ],
  rssLink: [
    {
      required: true,
      message: "请输入rss链接"
    }
  ],
  downloaderCategory: [
    {
      required: true,
      message: "请选择下载器"
    }
  ],
  episodeTitleRenameMethod: [
    {
      required: true,
      message: "请选择剧集重命名方式"
    }
  ]
};

// rss链接表单的列
const rssLinkColumns: PlusColumn[] = [
  {
    label: "类别",
    width: 120,
    prop: "rssCategory",
    valueType: "select",
    options: RssBangumRssCategoryOptions,
    colProps: {
      span: 6
    }
  },
  {
    label: "方式",
    width: 120,
    prop: "handleMethod",
    valueType: "select",
    options: RssBangumiHandleMethodOptions,
    colProps: {
      span: 6
    }
  },

  {
    label: "下载器",
    width: 120,
    prop: "downloaderCategory",
    valueType: "select",
    options: DownloaderCategoryOptions,
    colProps: {
      span: 6
    }
  },

  {
    label: "rss链接名",
    width: 120,
    prop: "rssName",
    valueType: "copy",
    colProps: {
      span: 6
    }
  },
  {
    label: "rss链接",
    width: 120,
    prop: "rssLink",
    valueType: "copy",
    colProps: {
      span: 16
    }
  },
  {
    label: "跳过的剧集",
    width: 120,
    prop: "skippedEpisodeNo",
    valueType: "input-number",
    colProps: {
      span: 8
    }
  },
  {
    label: "剧集重命名方式",
    width: 120,
    prop: "episodeTitleRenameMethod",
    valueType: "select",
    options: EpisodeTitleRenameMethodOptions,
    colProps: {
      span: 8
    }
  },
  {
    label: "自定义剧集标题格式",
    width: 120,
    prop: "customizeRenamedEpisodeTitleFormat",
    valueType: "copy",
    tooltip: "支持的占位符: {season}, {episode}",
    fieldProps: computed(() => ({
      disabled:
        rssLinkState.value.episodeTitleRenameMethod !==
        EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE
    })),
    colProps: {
      span: 16
    }
  },
  {
    label: "过滤规则",
    width: 120,
    prop: "filterRules",
    renderExtra: () =>
      `如果rss链接解析出来的原始标题包含对应规则，那么会过滤掉对应的数据`,
    valueType: "plus-input-tag",
    colProps: {
      span: 24
    }
  }
];

// 处理暂存
const handleTemporaryStorage = async (
  handleSubmit: () => Promise<boolean>,
  handleReset: () => void
) => {
  const isPass = await handleSubmit();
  console.log(isPass, "isPass");
  //isPass && (visible1.value = false)
};

const handlePreview = async (
  handleSubmit: () => Promise<boolean>,
  handleReset: () => void
) => {
  const isPass = await handleSubmit();
  if (!isPass) {
    return;
  }
  const req: RssBangumiPreviewReq = {
    rssCategory: rssLinkState.value.rssCategory as number,
    rssName: rssLinkState.value.rssName as string,
    rssLink: rssLinkState.value.rssLink as string,
    filterRules: rssLinkState.value.filterRules as Array<string>,
    handleMethod: rssLinkState.value.handleMethod as number,
    skippedEpisodeNo: rssLinkState.value.skippedEpisodeNo as number,
    downloaderCategory: rssLinkState.value.downloaderCategory as number,
    episodeTitleRenameMethod: rssLinkState.value
      .episodeTitleRenameMethod as number,
    customizeRenamedEpisodeTitleFormat: rssLinkState.value
      .customizeRenamedEpisodeTitleFormat as string
  };
  previewLoading.value = true;
  const resp: RssBangumiPreviewResp = await previewRssBangumi(req);
  previewLoading.value = false;

  if (!resp.success) {
    return;
  }
  message("rss bangumi preview success", { type: "success" });
  handleCloseForm(handleReset);
  // 抛出预览成功事件
  emit("preview-success", resp.data.id);
};

const handleCloseForm = async (handleReset: () => void) => {
  await handleReset();
  //initRssLinkState();
  visible.value = false;
  emit("close-form");
};
</script>
<template>
  <PlusDialogForm
    v-model:visible="visible"
    v-model="rssLinkState"
    :dialog="{ title: '新增Rss番剧', hasFooter: false }"
    :form="{
      columns: rssLinkColumns,
      rules: rssLinkRules,
      rowProps: { gutter: 20 },
      footerAlign: 'right',
      hasFooter: true,
      labelPosition: 'top'
    }"
  >
    <template #form-footer="{ handleSubmit, handleReset }">
      <el-button type="danger" @click="handleCloseForm(handleReset)">
        返回</el-button
      >
      <!-- <el-button
        type="warning"
        @click="handleTemporaryStorage(handleSubmit, handleReset)"
        >暂存</el-button
      > -->
      <el-button
        type="primary"
        :loading="previewLoading"
        @click="handlePreview(handleSubmit, handleReset)"
        >预览</el-button
      >
    </template>
  </PlusDialogForm>
</template>
