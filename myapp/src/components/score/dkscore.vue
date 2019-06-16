<template>
    <div>
        <x-header>成绩查询</x-header>
        <v-chart :data="data" ref="demo2">
            <v-scale x field="test_name"/>
            <v-scale y field="score"/>
            <v-point :style="{stroke: '#fff', lineWidth: 1}" shape="smooth"/>
            <v-line/>
        </v-chart>
        <picker :data="subjects" v-model="subjects" @on-change="change"></picker>
    </div>
</template>
<script>
import { XHeader, Cell, Group, Picker, GroupTitle, XButton } from "vux";
import { VChart, VLine, VPoint, VScale, VTooltip } from "vux";

export default {
    data() {
        return {
            data: [],
            subjects: [
                [
                    {
                        name: "语文",
                        value: "语文"
                    },
                    {
                        name: "数学",
                        value: "数学"
                    },
                    {
                        name: "英语",
                        value: "英语"
                    },
                    {
                        name: "化学",
                        value: "化学"
                    },
                    {
                        name: "物理",
                        value: "物理"
                    },
                    {
                        name: "政治",
                        value: "政治"
                    },
                    {
                        name: "历史",
                        value: "历史"
                    },
                    {
                        name: "生物",
                        value: "生物"
                    },
                    {
                        name: "附加题",
                        value: "附加题"
                    }
                ]
            ]
        };
    },
    components: {
        XHeader,
        Picker,
        GroupTitle,
        XButton,
        Cell,
        Group,
        VChart,
        VPoint,
        VLine,
        VScale,
        VTooltip
    },
    methods: {
        getData(subject) {
            this.data = [];
            this.$vux.loading.show({
                text: "正在加载"
            });
            this.$axios
                .get("/ssm/pastlessonrank", {
                    params: {
                        id: "10000000141",
                        lesson: subject[0],
                        type: "classrank"
                    }
                })
                .then(res => {
                    let tmp = res.data;

                    for (var i = 0; i < tmp.length; i++) {
                        let ele = tmp[i];
                        this.data.push({
                            test_name: ele.test_name,
                            score: ele.lesson_score
                        });
                    }
                    this.$vux.loading.hide();
                    this.$refs.demo2.rerender();
                })
                .catch(err => {
                    console.log("err");
                    this.$vux.loading.hide();
                });
        },
        change(val) {
            console.log(val);
            this.getData(val);
        }
    },
    created() {
        this.getData(["语文"]);
    }
};
</script>