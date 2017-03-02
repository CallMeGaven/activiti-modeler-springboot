/**
 * Created by cyrus on 2017/2/12.
 */
import { NgModule }      from '@angular/core';
import { BrowserModule } from '/@angular/platform-browser';

import { AppComponent }  from './index.component.ts';
import { TextNameComponent }  from './index.component.ts';
@NgModule({
    imports: [ BrowserModule ],
    declarations: [ AppComponent,TextNameComponent ],
    bootstrap: [ AppComponent,TextNameComponent ]
})
export class AppModule { }