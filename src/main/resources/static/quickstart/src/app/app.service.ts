import { Injectable }    from '@angular/core';
import { Headers,Http,Response,RequestOptions,ResponseType,RequestOptionsArgs } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { Mobile } from './app.module';

@Injectable()
export class AppService {

  private headers = new Headers({'Content-Type': 'application/json','Access-Control-Allow-Origin':'*'});
  private appUrl = 'http://localhost:80/list';   //URL to web api
  constructor(private http:Http) { }

  getHeroes() {
    return this.http.get(this.appUrl)
               .toPromise()
               .then(res=>res.json())
               .catch(this.handleError);
  }


  private handleError(error: any): Promise<any> {
  console.error('An error occurred', error); // for demo purposes only
  return Promise.reject(error.message || error);
  }

  getTask(url:string) {

    return this.http.get(url)
               .toPromise()
               .then(res=>res.json())
               .catch(this.handleError);
  }
}