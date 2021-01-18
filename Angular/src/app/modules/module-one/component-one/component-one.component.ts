import { Component, OnInit } from '@angular/core';
import { TestserviceService } from '../../../core/http/testrest/testservice.service';
import { User } from '../../../shared/models/user';
import { GraphService } from '../../../core/graph.service';

@Component({
  selector: 'app-component-one',
  templateUrl: './component-one.component.html',
  styleUrls: ['./component-one.component.scss']
})
export class ComponentOneComponent implements OnInit {

  message: string;
  user: User;

  constructor(private service: TestserviceService, private graphService: GraphService) { }

  ngOnInit(): void {
    this.user = this.graphService.user;
  }

  testJavaApi() {
    this.service.testJavaApi().subscribe(mess => {
      console.log(mess);
      this.message = mess.response;
    })
  }
}
