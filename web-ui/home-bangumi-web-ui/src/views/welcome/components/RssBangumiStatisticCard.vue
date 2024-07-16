<script setup lang="ts">
import { ref, onBeforeMount, reactive } from "vue";
import { CaretBottom, CaretTop } from "@element-plus/icons-vue";

import {
  statisticOnHome,
  RssBangumiStatisticOnHomeResp
} from "@/api/rssBangumi";

defineOptions({
  name: "RssBangumiStatisticCard"
});

const data = reactive({
  /**
   * 下载中的剧集数量
   * */
  downloadingEpisodesCount: 0,
  /**
   * 各种原因失败的剧集数量
   * */
  failedEpisodesCount: 0,
  /**
   * 一周内有更新的bangumi数量
   * */
  updatedBangumiCountInWeek: 0,

  /**
   * 今天更新的bangumi数量
   * */
  updatedBangumiCountToday: 0,

  /**
   * 当前季度订阅的数量
   * */
  subscriptionsCountInCurQuarter: 0,

  /**
   * 前一个季度订阅的数量
   * */
  subscriptionsCountInPreQuarter: 0,

  /**
   * 收集的番剧数量
   * */
  collectedBangumiCount: 0,

  /**
   * 今日收集的番剧数量
   * */
  collectedBangumiCountToday: 0
});

const curYearAndQuarter = reactive({
  year: 2024,
  quarter: "春"
});

const loadStatisticData = async () => {
  const resp: RssBangumiStatisticOnHomeResp = await statisticOnHome();
  if (!resp.success) {
    return;
  }

  data.downloadingEpisodesCount = resp.data.downloadingEpisodesCount;
  data.failedEpisodesCount = resp.data.failedEpisodesCount;
  data.updatedBangumiCountInWeek = resp.data.updatedBangumiCountInWeek;
  data.updatedBangumiCountToday = resp.data.updatedBangumiCountToday;
  data.subscriptionsCountInCurQuarter =
    resp.data.subscriptionsCountInCurQuarter;
  data.subscriptionsCountInPreQuarter =
    resp.data.subscriptionsCountInPreQuarter;
  data.collectedBangumiCount = resp.data.collectedBangumiCount;
  data.collectedBangumiCountToday = resp.data.collectedBangumiCountToday;
};

function initCurrentYearAndQuarter() {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1; // getMonth() 返回的月份是从0开始的，所以需要加1
  const quarter = Math.ceil(month / 3); // 每个季度包含3个月

  curYearAndQuarter.year = year;

  switch (quarter) {
    case 1:
      curYearAndQuarter.quarter = "冬";
      break;
    case 2:
      curYearAndQuarter.quarter = "春";
      break;
    case 3:
      curYearAndQuarter.quarter = "夏";
      break;
    case 4:
      curYearAndQuarter.quarter = "秋";
      break;
  }
}

onBeforeMount(() => {
  loadStatisticData();
  initCurrentYearAndQuarter();
});
</script>

<template>
  <el-row :gutter="16">
    <el-col :span="6">
      <div class="statistic-card">
        <el-statistic :value="data.downloadingEpisodesCount">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              下载中的剧集
            </div>
          </template>
        </el-statistic>
        <div class="statistic-footer">
          <div class="footer-item">
            <span>失败剧集</span>
            <span class="green" v-if="data.failedEpisodesCount <= 0">
              {{ data.failedEpisodesCount }}
            </span>
            <span class="red" v-else>
              {{ data.failedEpisodesCount }}
              <el-icon>
                <CaretTop />
              </el-icon>
            </span>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="6">
      <div class="statistic-card">
        <el-statistic :value="data.updatedBangumiCountInWeek">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              本周更新的番剧
            </div>
          </template>
        </el-statistic>
        <div class="statistic-footer">
          <div class="footer-item">
            <span>今日更新</span>
            <span class="green">
              {{ data.updatedBangumiCountToday }}
              <el-icon v-if="data.updatedBangumiCountToday > 0">
                <CaretTop />
              </el-icon>
            </span>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="6">
      <div class="statistic-card">
        <el-statistic :value="data.subscriptionsCountInCurQuarter">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              {{ curYearAndQuarter.year }}年{{
                curYearAndQuarter.quarter
              }}季订阅番剧
            </div>
          </template>
        </el-statistic>
        <div class="statistic-footer">
          <div class="footer-item">
            <template
              v-if="
                data.subscriptionsCountInCurQuarter >
                data.subscriptionsCountInPreQuarter
              "
            >
              <span>比上个季度增加</span>
              <span class="green">
                {{
                  data.subscriptionsCountInCurQuarter -
                  data.subscriptionsCountInPreQuarter
                }}
                <el-icon>
                  <CaretTop />
                </el-icon>
              </span>
            </template>
            <template
              v-else-if="
                data.subscriptionsCountInCurQuarter <
                data.subscriptionsCountInPreQuarter
              "
            >
              <span>比上个季度减少</span>
              <span class="red">
                {{
                  data.subscriptionsCountInPreQuarter -
                  data.subscriptionsCountInCurQuarter
                }}
                <el-icon>
                  <CaretBottom />
                </el-icon>
              </span>
            </template>
            <template v-else>
              <span>与上个季度持平</span>
            </template>
          </div>
        </div>
      </div>
    </el-col>
    <!-- <el-col :span="6">
      <div class="statistic-card">
        <el-statistic :value="693700">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              2024年订阅番剧
            </div>
          </template>
        </el-statistic>
        <div class="statistic-footer">
          <div class="footer-item">
            <span>比去年新增</span>
            <span class="red">
              120
              <el-icon>
                <CaretBottom />
              </el-icon>
            </span>
          </div>
        </div>
      </div>
    </el-col> -->
    <el-col :span="6">
      <div class="statistic-card">
        <el-statistic :value="data.collectedBangumiCount">
          <template #title>
            <div style="display: inline-flex; align-items: center">
              收集的番剧
            </div>
          </template>
        </el-statistic>
        <div class="statistic-footer">
          <div class="footer-item">
            <span>今日新增</span>
            <span class="green">
              {{ data.collectedBangumiCountToday }}
              <el-icon v-if="data.collectedBangumiCountToday > 0">
                <CaretTop />
              </el-icon>
            </span>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped>
:global(h2#card-usage ~ .example .example-showcase) {
  background-color: var(--el-fill-color) !important;
}

.el-statistic {
  --el-statistic-content-font-size: 28px;
}

.statistic-card {
  height: 100%;
  padding: 20px;
  border-radius: 4px;
  background-color: var(--el-bg-color-overlay);
}

.statistic-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  font-size: 12px;
  color: var(--el-text-color-regular);
  margin-top: 16px;
}

.statistic-footer .footer-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.statistic-footer .footer-item span:last-child {
  display: inline-flex;
  align-items: center;
  margin-left: 4px;
}

.green {
  color: var(--el-color-success);
}
.red {
  color: var(--el-color-error);
}
</style>
