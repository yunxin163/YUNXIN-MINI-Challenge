<template>
    <div>
        <x-header>上传记录</x-header>

        <!-- <b-table :items="items" :fields="fields" striped>
            <template slot="show_details" slot-scope="row">
                <b-button
                    size="sm"
                    @click="row.toggleDetails"
                    class="mr-2"
        >{{ row.detailsShowing ? 'Hide' : 'Show'}} Details</b-button>-->

        <!-- As `row.showDetails` is one-way, we call the toggleDetails function on @change -->
        <!-- <b-form-checkbox
                    v-model="row.detailsShowing"
                    @change="row.toggleDetails"
                >Details via check</b-form-checkbox>
            </template>

            <template slot="row-details" slot-scope="row">
                <b-card>
                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right">
                            <b>Age:</b>
                        </b-col>
                        <b-col>{{ row.item.age }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right">
                            <b>Is Active:</b>
                        </b-col>
                        <b-col>{{ row.item.isActive }}</b-col>
                    </b-row>

                    <b-button size="sm" @click="row.toggleDetails">Hide Details</b-button>
                </b-card>
            </template>
        </b-table>-->
        <b-table striped hover :items="recordlist" :fields="fields"></b-table>
        <!-- <input type="button" value="test" @click="GetRecord()"> -->
    </div>
</template>

<script>
import { XHeader } from "vux";
import { XTable } from "vux";
export default {
    components: {
        XHeader,
        XTable
    },
    data() {
        return {
            fields: {
                test_name: {
                    label: "考试名称"
                },
                lesson_name: {
                    label: "学科"
                },
                kownledge_point: {
                    label: "知识点"
                },
                problem_id: {
                    label: "题号"
                },
                uploadtime: {
                    label: "上传时间"
                }
            },
            // fields: ['test_name', 'lesson_name', 'kownledge_point','problem_id','uploadtime'],
            items: [
                {
                    isActive: true,
                    age: 40,
                    first_name: "Dickerson",
                    last_name: "Macdonald"
                },
                {
                    isActive: false,
                    age: 21,
                    first_name: "Larsen",
                    last_name: "Shaw"
                },
                {
                    isActive: false,
                    age: 89,
                    first_name: "Geneva",
                    last_name: "Wilson",
                    _showDetails: true
                },
                {
                    isActive: true,
                    age: 38,
                    first_name: "Jami",
                    last_name: "Carney"
                }
            ],
            recordlist: []
        };
    },
    methods: {
        GetRecord() {
            const _this = this;
            _this.$axios
                .get("/ssm/geterrorcollection", {
                    params: {
                        school: "just",
                        id: "10000000141"
                    }
                })
                .then(res => {
                    console.log(res.data);
                    _this.recordlist = res.data;
                })
                .catch(err => {});
        }
    },
    created(){
        this.GetRecord();
    }
};
</script>