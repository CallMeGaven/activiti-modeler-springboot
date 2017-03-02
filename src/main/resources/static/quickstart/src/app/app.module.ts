import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import { TextNameComponent }  from './app.component';
import { MyAjax }  from './app.component';
import { HttpModule }    from '@angular/http';
import { FormsModule }    from '@angular/forms';
import { AppService }          from './app.service';
import { Start }          from './app.component';
import { GetTask,Complete }          from './app.component';
export class Mobile {
    constructor(public name: string,description:string,revision:number) { }
}

@NgModule({
  imports:      [ BrowserModule,
                  HttpModule,
                  FormsModule ],
  declarations: [ AppComponent,
                  TextNameComponent,
                  MyAjax,
                  Start,
                  GetTask,
                  Complete ],
  bootstrap:    [ AppComponent,
                  TextNameComponent,
                  MyAjax,
                  Start,
                  GetTask,
                  Complete ],
  providers: [ AppService ]
})
export class AppModule { }


