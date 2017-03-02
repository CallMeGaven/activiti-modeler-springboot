import { Component,OnInit } from '@angular/core';
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
    template:  `<input #name (blur)="addName(name.value); name.value='' ">
                 <input #desp (blur)="addDesp(desp.value); desp.value='' ">
               <button (click)=add(desp.value)>Add</button>`
})
export  class TextNameComponent{
     named="";
     despd="";
    addName(name:string){
        this.named = name;
    }
    addDesp(desp:string){
        this.despd = desp;
    }
    add(){
        var hre = "http://localhost:80/create?name=" + this.named + "&key=1&description="+ this.despd;
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
    template:  `<input #processid (blur)="addName(processid.value); processid.value='' ">
               <button (click)=add(processid.value)>start</button>`
})
export  class Start{
     named="";

    addName(name:string){
        this.named = name;
    }
    add(){
        var hre = "http://localhost:80/startProcessInstance/" + this.named;
        location.href = hre;
    }

}

@Component({
    selector: 'get-task',
    template: `  
    <button (click)=add()>获取Task</button> 
           <ul>
           <li *ngFor="let m of heroes"><span>name: {{m.name}}</span><br>description:{{m.description}}</li>
            </ul>`
,
  providers: [AppService]
})
export class GetTask implements OnInit
{
  heroes: string;
  constructor(private heroService: AppService) { }
  add():void{
    this.heroService.getTask("http://localhost:80/getTask/management").then(heroes => this.heroes = heroes);
  }
    ngOnInit(): void {
    this.add();
  }
}
