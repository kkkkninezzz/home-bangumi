<script setup lang="ts">
import { ref, Ref, onBeforeMount, computed, h } from "vue";
import { ElMessageBox, ElSwitch } from "element-plus";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import {
  RssBangumRssCategoryOptions,
  RssBangumiHandleMethodOptions,
  RssBangumiHandleMethodEnum,
  RssBangumiStatusEnum,
  DownloaderCategoryEnum,
  DownloaderCategoryOptions,
  EpisodeTitleRenameMethodEnum,
  EpisodeTitleRenameMethodOptions
} from "../enums";

import { ApiResult } from "@/api/base";

import {
  RssBangumiDetailResp,
  getRssBangumiDetail,
  RssBangumiUpdateReq,
  updateRssBangumi,
  BangumiInfoDto,
  reparseBangumiEpisodes,
  ReparseBangumiEpisodesResp,
  activeRssBangumi,
  inactiveRssBangumi,
  archiveRssBangumi,
  pushToDownloader,
  incrementalParseBangumiEpisodes,
  IncrementalParseBangumiEpisodesResp
} from "@/api/rssBangumi";
import { message } from "@/utils/message";

defineOptions({
  name: "RssBangumiCardDetailPageBangumiForm"
});

const props = withDefaults(
  defineProps<{
    rssBangumiId: number;
    key?: number;
  }>(),
  {}
);

// 按钮事件通知父组件
const emit = defineEmits<{
  /** 重新解析成功 */
  (e: "reparse-success"): void;
  /** 增量解析 */
  (e: "incremental-parse-success"): void;
  /** 归档成功 */
  (e: "archive-success"): void;
  /** 收集 */
  (e: "collect-success"): void;
  /** 启用订阅 */
  (e: "active-success"): void;
  /** 暂停订阅 */
  (e: "inactive-success"): void;
}>();

const submitLoading = ref(false);
const archiveLoading = ref(false);
const reparseLoading = ref(false);
const incrementalParseLoading = ref(false);
const collectLoading = ref(false);
const activeLoading = ref(false);
const inactiveLoading = ref(false);
const reqButtonDisable = ref(false);

// 提交rss链接相关的表单
const rssBangumiState = ref<FieldValues>({
  id: null, // id
  rssCategory: null, // rss链接类型
  handleMethod: null, // 处理方式
  downloaderCategory: DownloaderCategoryEnum.QBITTORRENT, // 下载器分类
  status: null, //状态
  rssName: "", // 链接名
  rssLink: "", // 实际链接
  filterRules: [], // 过滤规则
  skippedEpisodeNo: 0, // 跳过的剧集号
  bangumiTitle: "", // 番剧名
  episodeTitleRenameMethod: EpisodeTitleRenameMethodEnum.TORRENT_PARSED_TITLE, // 剧集解析方式
  customizeRenamedEpisodeTitleFormat: "", // 自定义的重命名后标题格式
  bangumiPosterUrl: "", // 海报链接
  broadcastDayOfWeek: null, // 放送的星期几 从1开始到7
  broadcastDate: null, // 开始放送的日期
  season: 0 // 放送的是哪一季
});

// 初始化rss链接的表单
async function initRssBangumiForm(id?: number) {
  const resp: RssBangumiDetailResp = await getRssBangumiDetail(id);
  if (!resp.success) {
    return;
  }
  setRssBangumiState(resp);
}

// 初始化rss链接的表单状态对象
function setRssBangumiState(resp: RssBangumiDetailResp) {
  rssBangumiState.value.id = resp.data.id;
  rssBangumiState.value.rssCategory = resp.data.rssCategory;
  rssBangumiState.value.handleMethod = resp.data.handleMethod;
  rssBangumiState.value.downloaderCategory = resp.data.downloaderCategory;
  rssBangumiState.value.status = resp.data.status;
  rssBangumiState.value.rssName = resp.data.rssName;
  rssBangumiState.value.rssLink = resp.data.rssLink;
  rssBangumiState.value.filterRules = resp.data.filterRules;
  rssBangumiState.value.skippedEpisodeNo = resp.data.skippedEpisodeNo;
  rssBangumiState.value.episodeTitleRenameMethod =
    resp.data.episodeTitleRenameMethod;
  rssBangumiState.value.customizeRenamedEpisodeTitleFormat =
    resp.data.customizeRenamedEpisodeTitleFormat;

  rssBangumiState.value.bangumiTitle = resp.data.bangumiInfo?.title ?? "";
  rssBangumiState.value.bangumiScrapedTitle =
    resp.data.bangumiInfo?.renamedTitle ?? "";
  rssBangumiState.value.bangumiPosterUrl =
    resp.data.bangumiInfo?.posterUrl ?? "";
  rssBangumiState.value.broadcastDayOfWeek =
    resp.data.bangumiInfo?.broadcastDayOfWeek ?? null;
  rssBangumiState.value.broadcastDate =
    resp.data.bangumiInfo?.broadcastDate ?? null;
  rssBangumiState.value.season = resp.data.bangumiInfo?.season ?? 0;
}

onBeforeMount(() => {
  initRssBangumiForm(props.rssBangumiId);
});

// rss链接相关规则
const rssBangumiRules = {
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
  episodeTitleRenameMethod: [
    {
      required: true,
      message: "请选择剧集重命名方式"
    }
  ]
};

// rss链接表单的列
const rssBangumiColumns: PlusColumn[] = [
  {
    label: "类别",
    width: 120,
    prop: "rssCategory",
    valueType: "select",
    options: RssBangumRssCategoryOptions,
    colProps: {
      span: 6
    },
    fieldProps: { disabled: true }
  },
  {
    label: "方式",
    width: 120,
    prop: "handleMethod",
    valueType: "select",
    options: RssBangumiHandleMethodOptions,
    colProps: {
      span: 6
    },
    fieldProps: { disabled: true }
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
    },
    fieldProps: { disabled: true }
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
    label: "番剧标题",
    width: 120,
    prop: "bangumiTitle",
    valueType: "copy",
    colProps: {
      span: 10
    }
  },
  {
    label: "剧集重命名方式",
    width: 120,
    prop: "episodeTitleRenameMethod",
    valueType: "select",
    options: EpisodeTitleRenameMethodOptions,
    colProps: {
      span: 4
    }
  },
  {
    label: "自定义剧集标题格式",
    width: 120,
    prop: "customizeRenamedEpisodeTitleFormat",
    valueType: "copy",
    fieldProps: computed(() => ({
      disabled:
        rssBangumiState.value.episodeTitleRenameMethod !==
        EpisodeTitleRenameMethodEnum.CUSTOMIZED_TITLE
    })),
    colProps: {
      span: 10
    },
    renderExtra: () =>
      `支持的占位符: {title}, {season}, {episode}。 \n例如: {title} S{season}E{episode}`
  },
  {
    label: "季度",
    width: 120,
    prop: "season",
    valueType: "input-number",
    colProps: {
      span: 8
    }
  },
  {
    label: "首播时间",
    prop: "broadcastDate",
    valueType: "date-picker",
    colProps: {
      span: 8
    }
  },
  {
    label: "放送星期",
    width: 120,
    prop: "broadcastDayOfWeek",
    valueType: "select",
    options: [
      {
        label: "Mon.",
        value: 1,
        color: "blue"
      },
      {
        label: "Tues.",
        value: 2,
        color: "blue"
      },
      {
        label: "Wed.",
        value: 3,
        color: "blue"
      },
      {
        label: "Thur.",
        value: 4,
        color: "blue"
      },
      {
        label: "Fri.",
        value: 5,
        color: "blue"
      },
      {
        label: "Sat.",
        value: 6,
        color: "blue"
      },
      {
        label: "Sun.",
        value: 7,
        color: "blue"
      }
    ],
    colProps: {
      span: 8
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

const beforeReqForButton = (buttonLoading: Ref<boolean>) => {
  buttonLoading.value = true;
  reqButtonDisable.value = true;
};

const afterReqForButton = (buttonLoading: Ref<boolean>) => {
  buttonLoading.value = false;
  reqButtonDisable.value = false;
};

const handleSubmit = async (values: FieldValues) => {
  const bangumiInfo: BangumiInfoDto = {
    title: rssBangumiState.value.bangumiTitle as string,
    renamedTitle: rssBangumiState.value.bangumiScrapedTitle as string,
    posterUrl: rssBangumiState.value.bangumiPosterUrl as string,
    broadcastDayOfWeek: rssBangumiState.value.broadcastDayOfWeek as number,
    broadcastDate: rssBangumiState.value.broadcastDate as number,
    season: rssBangumiState.value.season as number
  };
  const req: RssBangumiUpdateReq = {
    rssName: rssBangumiState.value.rssName as string,
    filterRules: rssBangumiState.value.filterRules as Array<string>,
    skippedEpisodeNo: rssBangumiState.value.skippedEpisodeNo as number,
    downloaderCategory: rssBangumiState.value.downloaderCategory as number,
    episodeTitleRenameMethod: rssBangumiState.value
      .episodeTitleRenameMethod as number,
    customizeRenamedEpisodeTitleFormat: rssBangumiState.value
      .customizeRenamedEpisodeTitleFormat as string,
    bangumiInfo: bangumiInfo
  };

  beforeReqForButton(submitLoading);
  const resp: RssBangumiDetailResp = await updateRssBangumi(
    props.rssBangumiId,
    req
  );
  afterReqForButton(submitLoading);
  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setRssBangumiState(resp);
};

// 处理归档
const handleArchive = async () => {
  const deleteFile = ref<boolean>(false);
  ElMessageBox({
    title: "归档rss番剧",
    message: () =>
      h("div", null, [
        h("p", null, [
          h(
            "span",
            null,
            "确定要归档吗？归档后不可恢复，同时会删除提交到下载器中的种子文件"
          )
        ]),
        h("p", null, [
          h(ElSwitch, {
            modelValue: deleteFile.value,
            "onUpdate:modelValue": (val: boolean) => {
              deleteFile.value = val;
            },
            activeText: "删除下载完成的剧集文件"
          })
        ])
      ]),
    showCancelButton: true,
    confirmButtonText: "确认",
    cancelButtonText: "取消"
  })
    .then(() => {
      doHandleArchive(deleteFile.value);
    })
    .catch(() => {});
};

const doHandleArchive = async (deleteFile: boolean) => {
  beforeReqForButton(archiveLoading);
  const resp: ApiResult = await archiveRssBangumi(
    props.rssBangumiId,
    deleteFile
  );
  afterReqForButton(archiveLoading);
  if (!resp.success) {
    return;
  }
  message("归档成功", { type: "success" });
  emit("archive-success");
};

// 处理重新解析
const handleReparse = async () => {
  ElMessageBox.confirm(
    "确认重新解析全部剧集信息吗？重新解析将删除目前所有的剧集信息，如果提交到下载器中的种子也将会一并删除，订阅状态也将重置为暂停中",
    "重新解析",
    {
      type: "warning",
      center: true
    }
  )
    .then(() => {
      doHandleReparse();
    })
    .catch(() => {});
};

const doHandleReparse = async () => {
  beforeReqForButton(reparseLoading);
  const resp: ReparseBangumiEpisodesResp = await reparseBangumiEpisodes(
    props.rssBangumiId
  );
  afterReqForButton(reparseLoading);
  if (!resp.success) {
    return;
  }
  message("重新解析成功", { type: "success" });
  emit("reparse-success");
};

// 处理增量解析
const handleIncrementalParse = async () => {
  beforeReqForButton(incrementalParseLoading);
  const resp: IncrementalParseBangumiEpisodesResp =
    await incrementalParseBangumiEpisodes(props.rssBangumiId);
  afterReqForButton(incrementalParseLoading);
  if (!resp.success) {
    return;
  }
  message("增量解析成功", { type: "success" });
  emit("incremental-parse-success");
};

// 处理立即收集
const handleCollect = async () => {
  beforeReqForButton(collectLoading);
  const resp: ApiResult = await pushToDownloader(props.rssBangumiId);
  afterReqForButton(collectLoading);
  if (!resp.success) {
    return;
  }
  message("已推送至下载器中", { type: "success" });
  emit("collect-success");
};

// 处理启用
const handleActive = async () => {
  beforeReqForButton(activeLoading);
  const resp: ApiResult = await activeRssBangumi(props.rssBangumiId);
  afterReqForButton(activeLoading);
  if (!resp.success) {
    return;
  }
  message("启用订阅成功", { type: "success" });
  emit("active-success");
};

// 处理暂停
const handleInactive = async () => {
  beforeReqForButton(inactiveLoading);
  const resp: ApiResult = await inactiveRssBangumi(props.rssBangumiId);
  afterReqForButton(inactiveLoading);
  if (!resp.success) {
    return;
  }
  message("暂停订阅成功", { type: "success" });
  emit("inactive-success");
};
</script>
<template>
  <span :key="key" />
  <PlusForm
    v-model="rssBangumiState"
    :disabled="RssBangumiStatusEnum.ARCHIVED == rssBangumiState.status"
    label-position="top"
    :columns="rssBangumiColumns"
    :rules="rssBangumiRules"
    :row-props="{ gutter: 20 }"
    footerAlign="right"
    @submit="handleSubmit"
  >
    <template #footer="{ handleSubmit }">
      <div style="margin: 0 auto">
        <el-button
          v-if="RssBangumiStatusEnum.ARCHIVED != rssBangumiState.status"
          type="danger"
          :loading="archiveLoading"
          :disabled="reqButtonDisable"
          @click="handleArchive"
          >归档</el-button
        >
        <el-button
          type="danger"
          :loading="reparseLoading"
          :disabled="reqButtonDisable"
          @click="handleReparse"
          >重新解析</el-button
        >
        <template
          v-if="
            RssBangumiHandleMethodEnum.COLLECT == rssBangumiState.handleMethod
          "
        >
          <el-button
            type="primary"
            :loading="collectLoading"
            :disabled="reqButtonDisable"
            @click="handleCollect"
            >立即收集</el-button
          >
        </template>
        <template
          v-else-if="
            RssBangumiHandleMethodEnum.SUBSCRIBE == rssBangumiState.handleMethod
          "
        >
          <el-button
            type="primary"
            :loading="incrementalParseLoading"
            :disabled="reqButtonDisable"
            @click="handleIncrementalParse"
            >增量解析</el-button
          >
          <el-button
            v-if="RssBangumiStatusEnum.INACTIVE == rssBangumiState.status"
            type="primary"
            :loading="activeLoading"
            :disabled="reqButtonDisable"
            @click="handleActive"
            >启用</el-button
          >
          <el-button
            v-else
            type="warning"
            :loading="inactiveLoading"
            :disabled="reqButtonDisable"
            @click="handleInactive"
            >暂停</el-button
          >
        </template>

        <el-button
          type="primary"
          :loading="submitLoading"
          :disabled="reqButtonDisable"
          @click="handleSubmit"
          >保存</el-button
        >
      </div>
    </template>
  </PlusForm>
</template>
