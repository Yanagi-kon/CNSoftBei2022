<template>
  <el-row style="margin: 20px;">
<!--    <el-button type="primary" style="margin: 5px;" @click="addNoticeClick">添加留言公告信息</el-button>-->
    <h2>用户意见</h2>
    <el-table
      :data="noticeInfo.content"
      style="width: 100%;margin-top: 20px;">

      <el-table-column
        prop="title"
        label="标题">
      </el-table-column>

      <el-table-column
        prop="message"
        label="内容">
      </el-table-column>

      <el-table-column
        prop="user.name"
        label="用户名">
      </el-table-column>

      <el-table-column
        prop="type"
        label="类型">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.type === 1 ? 'primary' : 'success'"
            disable-transitions>{{scope.row.type===1?'意见':'留言'}}</el-tag>
        </template>

      </el-table-column>

      <el-table-column
        label="创建时间"
        prop="createTime">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.createTime|formatDate}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="editNotice(scope.$index, scope.row)">回复</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="delNotice(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="10"
      :total="noticeInfo.totalElements"
      :page-count="noticeInfo.totalPage"
      @current-change="changeNoticePage">
    </el-pagination>

    <!--添加或者修改留言-->
    <el-dialog :title="noticeTitle" :visible.sync="showNotice">
      <el-form :model="notice" ref="noticeForm">
        <el-form-item v-if="!isAddNotice" label="id" :label-width="formLabelWidth">
          <el-input v-model="notice.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="标题" :label-width="formLabelWidth">
          <el-input v-model="notice.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="内容" :label-width="formLabelWidth">
          <el-input v-model="notice.message" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item  v-if="isAddNotice" label="发送用户" :label-width="formLabelWidth">
          <el-select placeholder="发送用户">
            <el-option v-for="(item,index) in sendUser" :key="index" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类型" prop="sex" :label-width="formLabelWidth">
          <el-col :span="8">
            <el-radio-group v-model="notice.type">
              <el-radio-button label="1">公告</el-radio-button>
              <el-radio-button label="2">留言</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showNotice = false">取 消</el-button>
        <el-button v-if="isAddNotice" type="primary" @click="addNotice">添加</el-button>
        <el-button v-else type="primary" @click="updateNotice">修改</el-button>
      </div>
    </el-dialog>
  </el-row>
</template>

<script>
  import {getNoticePages,addNotice,updateNotice,delNotice} from '@/api/notice'
  import {getDepartmentNames} from '@/api/department';
  import {parseTime} from "@/utils";
  export default {
    name: 'Admin',
    data(){
      return{
        activeName:"account",
        formLabelWidth: "150px",
        noticeInfo:{
          content:[],//数据
          currentPage:0,//当前页数
          totalElements:0,//总条数
          totalPage:0//总页数
        },
        //信息
        notice:{
          id:null,
          name:"",
          note:"",
          money:null,
          startTime:"",
          endTime:"",
          department:{
            id:null,
            name:""
          }
        },
        showNotice:false,
        isAddNotice:true,
        noticeTitle:"添加信息",
        pageSize:10,
        departmentNames:[],
        sendUser:[
          {id:1,name:"admin"},
          {id:2,name:"test"},
          {id:3,name:"guest"},
        ]
      };
    },
    mounted(){
      this.getNoticePages(this.noticeInfo.currentPage);
      this.getDepartmentNames();
    },
    methods:{

      getNoticePages(page){
        getNoticePages(page,this.pageSize).then(response => {
          const { data} = response
          this.noticeInfo = data;
          console.log(data)
        }).catch(error => {
          console.log(error)
        });
      },
      //留言分页
      changeNoticePage(page){
        console.log("切换页码"+page)
        this.getNoticePages(page-1);
      },
      addNoticeClick(){
        this.showNotice = true;
        this.noticeTitle="添加留言信息";
        this.isAddNotice = true;
        if (this.$refs["noticeForm"] !== undefined) {
          this.$refs["noticeForm"].resetFields();
        }
      },
      //添加留言信息
      addNotice(){
        addNotice(this.notice).then(response => {
          this.$message({
            message: "添加留言成功",
            type: "success"
          });
          if (this.$refs["noticeForm"] !== undefined) {
            this.$refs["noticeForm"].resetFields();
          }
          this.showNotice = false;
          this.getNoticePages(this.noticeInfo.currentPage);
        }).catch(error => {
          console.log(error)
        });
      },
      //更新留言信息
      editNotice(index,row){
        console.log(row);
        this.notice = row;
        this.showNotice = true;
        this.showNotice = true;
        this.isAddNotice = false
        this.noticeTitle="修改留言信息";
      },
      //修改留言信息
      updateNotice(){
        updateNotice(this.notice).then(response => {
          this.$message({
            message: "修改留言成功",
            type: "success"
          });
          if (this.$refs["noticeForm"] !== undefined) {
            this.$refs["noticeForm"].resetFields();
          }
          this.showNotice = false;
          this.getNoticePages(this.noticeInfo.currentPage);
        }).catch(error => {
          console.log(error)
        });
      },
      delNotice(index,row){
        delNotice(row.id).then(response => {
          this.$message({
            message: "删除留言成功",
            type: "success"
          });
          this.getNoticePages(0);
        }).catch(error => {
          console.log(error)
        });
      },
      getDepartmentNames(){
        getDepartmentNames().then(response => {
          this.departmentNames = response.data;
        }).catch(error => {
          console.log(error)
        });
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

</style>
