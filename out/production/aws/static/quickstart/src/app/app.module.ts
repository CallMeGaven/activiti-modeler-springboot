import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import { TextNameComponent }  from './app.component';
import { MyAjax }  from './app.component';
import { HttpModule }    from '@angular/http';
import { AppService }          from './app.service';
import { Start }          from './app.component';
import { GetTask }          from './app.component';
export class Mobile {
    constructor(public name: string,description:string,revision:number) { }
}

@NgModule({
  imports:      [ BrowserModule,
                  HttpModule ],
  declarations: [ AppComponent,
                  TextNameComponent,
                  MyAjax,
                  Start,
                  GetTask ],
  bootstrap:    [ AppComponent,
                  TextNameComponent,
                  MyAjax,
                  Start,
                  GetTask ],
  providers: [ AppService ]
})
export class AppModule { }


