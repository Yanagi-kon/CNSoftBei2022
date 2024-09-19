<template>
  <div class="all-container" style="margin-left: 20px;">
    <div class="all-container-padding bg" >
      <el-tabs v-model="activeName">
        <el-tab-pane label="团队信息" name="account">
          <el-row>
            <el-col :span="24">
              <el-card class="box-card">
                <el-col :span="18">
                  <el-col :span="24" style="margin-top: 20px;"><label>团队名:{{info.name}}</label></el-col>
                  <el-col :span="24" style="margin-top: 20px;"><label>团队地址:{{info.address}}</label></el-col>
                  <el-col :span="24" style="margin-top: 20px;"><label>团队人数:{{info.count}}</label></el-col>
                  <!--<el-col :span="24" style="margin-top: 20px;"><label>团队创建时间:{{info.createTime|formatDate}}</label></el-col>-->
                  <el-col :span="24" style="margin-top: 20px;"><label>团队介绍:勤能补拙</label></el-col>
                </el-col>
              </el-card>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
      </div>
     </div>
</template>

<script>
import {getInfo} from '@/api/company'
import {getMoneyInfoPages} from '@/api/moneyinfo'
import {getMoneyTypes,addMoneyTypes,updateMoneyTypes,delMoneyTypes} from '@/api/moneytype'
import {getDepartmentPages,addDepartment,updateDepartment,delDepartment} from '@/api/department'
import {parseTime} from "@/utils"
import "@/excel/Blob";
import "@/excel/export2Excel";
export default {
  name: 'Admin',
  data(){
    return{
      activeName:"account",
      formLabelWidth: "150px",
      info:{},
      departmentInfo:{
        content:[],//数据
        currentPage:0,//当前页数
        totalElements:0,//总条数
        totalPage:0//总页数
      },
      //信息
      department:{
        id:0,
        name:"",
        note:"",
        money:null,
        businessAmount:null
      },
      showDepartment:false,
      isAddDepartment:true,
      departmentTitle:"添加部门信息",
      pageSize:10,
      moneyTypeTitle:"添加类型",
      showMoneyType:false,
      isAddMoneyType:true,
      moneyType:{
        name:"",
        type:null
      }
    };
  },
  mounted(){
    this.getInfo();
  },

  methods:{
    getInfo(){
      getInfo().then(response => {
        const { data} = response
        this.info = data;
      }).catch(error => {
        console.log(error)
      });
    },
    //格式转换，直接复制即可
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => v[j]))
    }
  },
  filters:{
    formatDate:function(time) {
      if(time!=null&&time!=="")
      {
        // var date = new Date(time);
        return parseTime(time, '{y}-{m}-{d}');
      }else{
        return "";
      }
    }
  }
}
</script>


<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  padding: 20px;
}


</style>
