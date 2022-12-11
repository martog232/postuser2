import { Component, OnInit } from '@angular/core';
import { Image } from 'src/app/_models/image.model';
import { Post } from 'src/app/_models/post.model';

@Component({
  selector: 'app-image',
  templateUrl: './image.component.html',
  styleUrls: ['./image.component.css']
})
export class ImageComponent implements OnInit {

  emptyImage: Image = {
    id: undefined,
    url: ''
  }

  constructor() { }

  ngOnInit(): void {
  }

}
