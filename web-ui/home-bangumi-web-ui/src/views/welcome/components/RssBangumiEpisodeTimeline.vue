<script setup lang="ts">
import { shallowRef, onBeforeMount } from "vue";
import RssBangumiEpisodeTimelineCard from "./RssBangumiEpisodeTimelineCard.vue";
import { formatDate, isEarlierThanThisMonday } from "@/utils/date";
import {
  UpdatedDate2BangumisDto,
  UpdatedBangumisResp,
  UpdatedBangumiDto,
  getUpdatedBangumisForWeek
} from "@/api/rssBangumi";
import { DayOfWeekToDescMap } from "@/views/rssbangumi/enums";
import { RssBangumiEpisodeTimelineCardPropType } from "../props";

defineOptions({
  name: "RssBangumiEpisodeTimeline"
});

const updatedBangumisList = shallowRef(new Array<UpdatedDate2BangumisDto>());

const initUpdatedBangumisList = async () => {
  const resp: UpdatedBangumisResp = await getUpdatedBangumisForWeek();
  if (!resp.success) {
    return;
  }
  updatedBangumisList.value = resp.data.bangumis;
};

onBeforeMount(() => {
  initUpdatedBangumisList();
});

function formatTimestamp(updatedDate2BangumisDto: UpdatedDate2BangumisDto) {
  const prefix = isEarlierThanThisMonday(updatedDate2BangumisDto.date)
    ? "上周 "
    : "";
  return `${prefix}${DayOfWeekToDescMap.get(updatedDate2BangumisDto.dayOfWeek)}(${formatDate(updatedDate2BangumisDto.date)})`;
}

function toRssBangumiCardPropType(
  bangumi: UpdatedBangumiDto
): RssBangumiEpisodeTimelineCardPropType {
  return {
    id: bangumi.rssBangumiId,
    bangumiTitle: bangumi.bangumiTitle,
    bangumiPosterUrl: bangumi.bangumiPosterUrl,
    latestEpisodeNo: bangumi.latestEpisodeNo,
    latestUpdatedTime: bangumi.latestUpdatedTime,
    delay: bangumi.delay,
    actualBroadcastDayOfWeek: bangumi.actualBroadcastDayOfWeek,
    expectedBroadcastDayOfWeek: bangumi.expectedBroadcastDayOfWeek
  };
}
</script>

<template>
  <el-card shadow="never">
    <div class="flex justify-between rss-bangmui-episode-timeline-box-header">
      <span class="text-md font-medium">番剧更新动态</span>
    </div>
    <div style="height: 800px">
      <el-scrollbar max-height="800px">
        <el-timeline>
          <template
            v-for="(updatedDate2BangumisDto, index) in updatedBangumisList"
          >
            <el-timeline-item
              :timestamp="formatTimestamp(updatedDate2BangumisDto)"
              placement="top"
            >
              <el-empty
                v-if="
                  !updatedDate2BangumisDto.bangumis ||
                  updatedDate2BangumisDto.bangumis.length == 0
                "
                description="no data"
                :image-size="30"
              />
              <el-space v-else wrap :size="25">
                <template
                  v-for="(bangumi, index2) in updatedDate2BangumisDto.bangumis"
                >
                  <rss-bangumi-episode-timeline-card
                    :episode-card="toRssBangumiCardPropType(bangumi)"
                  />
                </template>
              </el-space>
            </el-timeline-item>
          </template>
        </el-timeline>
      </el-scrollbar>
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
.rss-bangmui-episode-timeline-box-header {
  margin-bottom: 15px;
}
</style>
