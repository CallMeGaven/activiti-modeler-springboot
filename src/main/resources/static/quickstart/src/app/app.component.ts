import { Component,OnInit,NgModule } from '@angular/core';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';
import { AppService }   from './app.service';
import { Mobile }       from './app.module'
@Component({
  selector: 'my-app',
  template: `<h1>Activiti {{name}}</h1>`,
})
export class AppComponent  { 
  name = 'Demo'; 
}

@Component({
    selector:'text-name',
    template:  `
    <input [(ngModel)]="name" placeholder="流程名称">
                 <input [(ngModel)]="desp " placeholder="描述">
               <button (click)=add(desp.value)>新建流程</button>`
})
export  class TextNameComponent{
     name:string = "";
     desp:string = "";
    add(){
        var hre = "http://localhost:80/create?name=" + this.name + "&key=1&description="+ this.desp;
        location.href = hre;
    }

}


@Component({
    selector: 'my-ajax',
    template: `  
    <button (click)=add()>获取</button> 
           <ul>
           <li *ngFor="let m of heroes"><span>name: {{m.name}}</span><br>description:{{m.description}}</li>
            </ul>`
,
  providers: [AppService]
})
export class MyAjax implements OnInit
{
  heroes: Object[];
  constructor(private heroService: AppService) { }
  add():void{
    this.heroService.getHeroes().then(heroes => this.heroes = heroes);
  }
    ngOnInit(): void {
    this.add();
  }
}


@Component({
    selector:'start',
    template:  `<input [(ngModel)]="id" placeholder="流程id">
               <button (click)=add(id.value)>启动流程</button>`
})
export  class Start{
     id:string = "";
    add(){
        var hre = "http://localhost:80/startProcessInstance/" + this.id;
        location.href = hre;
    }

}

@Component({
    selector: 'get-task',
    template: `  
    <button (click)=add()>获取Task</button> 
           <ul>
           <li *ngFor="let m of heroes"><span>name: {{m.name}}</span><br>id:{{m.id}}</li>
            </ul>`
,
  providers: [AppService]
})
export class GetTask implements OnInit
{
  heroes:Object[];
  constructor(private heroService: AppService) { }
  add():void{
    this.heroService.getTask("http://localhost:80/getTask/management").then(heroes => this.heroes = heroes);
  }
    ngOnInit(): void {
    this.add();
  }
}


@Component({
    selector:'complete',
    template:  `<input [(ngModel)]="id" placeholder="在任务列表中的index">
               <button (click)=add(id.value)>审批</button>`
})
export  class Complete{
     id:string = "";
    add(){
        var hre = "http://localhost:80/complete/" + this.id;
        location.href = hre;
    }

}