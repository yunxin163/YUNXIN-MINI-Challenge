<template>
    <div>
        <x-header>成绩分析</x-header>
        <group label-width="5em">
            <popup-picker
                :title="title1"
                :data="list1"
                v-model="value1"
                @on-show="onShow"
                @on-hide="onHide"
                @on-change="onChange"
            ></popup-picker>
            <popup-picker
                :title="title2"
                :data="list2"
                v-model="value2"
                @on-show="onShow"
                @on-hide="onHide"
                @on-change="onChange"
            ></popup-picker>
            <x-button type="primary" @click.native="rerender()">确认</x-button>
        </group>

        <div>
            <ve-pie :data="chartData" :settings="chartSettings" :legend-visible="false"></ve-pie>
        </div>
    </div>
</template>

<script>
import { XHeader } from "vux";
import {
    PopupPicker,
    Group,
    Cell,
    Picker,
    XButton,
    Divider,
    XSwitch
} from "vux";

export default {
    components: {
        XHeader,
        PopupPicker,
        Group,
        Picker,
        XButton,
        Divider,
        Cell,
        XSwitch
    },
    data() {
        this.chartSettings = {
            roseType: "area"
        };
        return {
            chartData: {
                columns: ["question_index", "score"],
                rows: []
            },
            title1: "选择科目",
            title2: "选择考试",
            list1: [
                [
                    "语文",
                    "数学",
                    "英语",
                    "物理",
                    "化学",
                    "政治",
                    "历史",
                    "地理",
                    "生物",
                    "附加题"
                ]
            ],
            list2: [
                ["test1", "test2", "test3", "test4", "test5", "test6", "test7"]
            ],
            value2: ["test1"],
            value1: ["语文"]
        };
    },
    methods: {
        onShow() {
            console.log("onshow");
        },
        onHide() {
            console.log("onshow");
        },
        onChange() {
            console.log("onshow");
        },
        rerender() {
            const _this = this;
            _this.$axios
                .get("/ssm/getproblemscorebystuid", {
                    params: {
                        lesson: _this.value1[0],
                        studentid: "10000000141",
                        testname: _this.value2[0]
                    }
                })
                .then(res => {
                    _this.chartData.rows = res.data;
                })
                .catch(err => {
                    console.log(err);
                });
        }
    },
    created() {
        this.rerender();
    }
};
</script>