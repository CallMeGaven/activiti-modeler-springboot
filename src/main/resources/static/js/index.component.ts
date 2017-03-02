/**
 * Created by cyrus on 2017/2/12.
 */
import { Component } from '@angular/core';

@Component({
    selector: 'my-app',
    template: `<h1>Hello {{name}}</h1>`
})
export class AppComponent { name = 'Angular'; }

@Component({
    selector: 'click-me',
    template: `<button (click)="onClickMe()">Click me!</button>
    {{clickMessage}}`
})
export class ClickMeComponent {
    clickMessage = '';
    onClickMe() {
        this.clickMessage = 'You are so 666!';
        //var hre = "/create?name=" + $scope.processName+ "&key=1&description="+$scope.processDesprtion;
        // location.href = hre;
    }
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
        var hre = "/create?name=" + this.named + "&key=1&description="+ this.despd;
        location.href = hre;
    }

}
