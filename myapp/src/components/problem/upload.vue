<template>
    <div>
        <x-header>错题上传</x-header>

        <div>
            <group label-width="5em">
                <popup-picker :title="title1" :data="list2" v-model="value2"></popup-picker>
            </group>
            <group label-width="5em">
                <popup-picker :title="title2" :data="list1" v-model="value1"></popup-picker>
            </group>
            <group label-width="5em">
                <popup-picker :title="title3" :data="list3" v-model="value3"></popup-picker>
            </group>
        </div>
        <div>
            <b-form-file
                v-model="file"
                :state="Boolean(file)"
                placeholder="选择一个文件"
                drop-placeholder="Drop file here..."
            ></b-form-file>
            <div class="mt-3">已选择的文件: {{ file ? file.name : '' }}</div>
        </div>
        <x-button type="primary" @click.native="query()">上传</x-button>
    </div>
</template>
<script>
import { XHeader, Group, PopupPicker, XButton } from "vux";
import { Toast } from "vux";
import { Loading } from "vux";
export default {
    components: {
        XHeader,
        Group,
        PopupPicker,
        XButton,
        Toast
    },
    data() {
        return {
            file: null,
            list1: [["语文", "数学", "英语", "物理", "化学"]],
            list2: [
                ["test1", "test2", "test3", "test4", "test5", "test6", "test7"]
            ],
            list3: [
                [
                    1,
                    2,
                    3,
                    4,
                    5,
                    6,
                    7,
                    8,
                    9,
                    10,
                    11,
                    12,
                    13,
                    14,
                    15,
                    16,
                    17,
                    18,
                    19,
                    20,
                    21,
                    22,
                    23,
                    24,
                    25,
                    26
                ]
            ],
            title1: "选择考试",
            title2: "学科选择",
            title3: "题号选择",
            value1: ["语文"],
            value2: ["test1"],
            value3: ["1"]
        };
    },
    methods: {
        upload() {
            const _this = this;
            let fd = new FormData();
            fd.append("studentid", "10000000141");
            fd.append("type", _this.value2[0]);
            fd.append("testname", _this.value2[0]);
            fd.append("testname", _this.value1[0]);

            _this.$axios.post("/ssm/onfile", fd).then(res);
        },
        query() {
            const _this = this;
            if (_this.file != null) {
                this.$vux.toast.show({
                    text: "上传成功"
                });
            } else {
                this.$vux.toast.show({
                    type: "warn",
                    text: "请选择文件"
                });
            }
        }
    }
};
</script>