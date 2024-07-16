<script setup lang="ts">
import { computed } from "vue";
import { ReText } from "@/components/ReText";
import { Warning } from "@element-plus/icons-vue";
import { RssBangumiEpisodeTimelineCardPropType } from "../props";
import { DayOfWeekToDescMap } from "@/views/rssbangumi/enums";
import { useDetailPage } from "@/views/rssbangumi/components/hooks";

defineOptions({
  name: "RssBangumiEpisodeTimelineCard"
});
const props = defineProps({
  episodeCard: {
    type: Object as PropType<RssBangumiEpisodeTimelineCardPropType>
  }
});

const { toDetailPage, router } = useDetailPage();

const getImageUrl = (posterUrl: string) => {
  if (posterUrl && posterUrl != "") {
    return import.meta.env.VITE_SERVER_BASE_URL + "/" + posterUrl;
  }
  return new URL("../../../assets/imgs/default-poster.png", import.meta.url)
    .href;
};

const handleClick = (id: number) => {
  // 跳转到番剧详情页
  toDetailPage({ id: id });
};

const tooltipContent = computed(() => {
  if (props.episodeCard.delay && props.episodeCard.actualBroadcastDayOfWeek) {
    return `更新延迟，预计${DayOfWeekToDescMap.get(props.episodeCard.expectedBroadcastDayOfWeek)}更新，实际: ${DayOfWeekToDescMap.get(props.episodeCard.actualBroadcastDayOfWeek)}`;
  }

  return "";
});
</script>

<template>
  <el-card style="width: 240px">
    <div class="message-header"></div>
    <template #header
      ><el-row>
        <el-col :span="18">
          <re-text
            ><el-tooltip
              v-if="episodeCard.delay && episodeCard.actualBroadcastDayOfWeek"
              effect="dark"
              :content="tooltipContent"
              placement="top"
            >
              <el-icon :size="12">
                <Warning />
              </el-icon>
            </el-tooltip>
            {{ episodeCard.bangumiTitle }}
          </re-text>
        </el-col>
        <el-col :span="6">
          <el-tag type="primary">
            第{{ episodeCard.latestEpisodeNo }}集
          </el-tag>
        </el-col>
      </el-row></template
    >
    <div class="list-card-item--poster">
      <img
        :src="getImageUrl(episodeCard.bangumiPosterUrl)"
        style="width: 100%"
        @click="handleClick(episodeCard.id)"
      />
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.list-card-item {
  &--poster {
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;

    border-radius: 2%;

    &__disabled {
      color: #a1c4ff;
    }
  }
}
</style>
