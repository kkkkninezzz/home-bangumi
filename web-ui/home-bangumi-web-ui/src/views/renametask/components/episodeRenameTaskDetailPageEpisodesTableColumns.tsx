import { EpisodeRenameTaskItemStatusTagMap } from "../enums";

export const TaskItemColumns: TableColumnList = [
  {
    label: "id",
    prop: "id",
    hide: true
  },
  {
    label: "剧集",
    prop: "episodeNo",
    width: "100",
    fixed: true
  },
  {
    label: "状态",
    prop: "status",
    width: "140",
    fixed: true,
    cellRenderer: ({ row }) => (
      <>
        <el-tag
          type={EpisodeRenameTaskItemStatusTagMap.get(row.status).tagType}
        >
          {EpisodeRenameTaskItemStatusTagMap.get(row.status).tagContent}
        </el-tag>
      </>
    )
  },
  {
    label: "剧集文件名",
    prop: "episodeFileName",
    width: "400"
  },
  {
    label: "重命名后的剧集文件名",
    prop: "renamedEpisodeFileName",
    width: "320"
  },
  {
    label: "剧集文件源路径",
    prop: "episodePath",
    width: "400"
  },
  {
    label: "重命名后的剧集输出路径",
    prop: "renamedEpisodeOutputPath",
    width: "320"
  },
  {
    label: "错误消息",
    prop: "errorMessage",
    width: "200"
  },
  {
    label: "操作",
    width: "180",
    fixed: "right",
    slot: "operation"
  }
];
