import { Component, Input, OnInit } from '@angular/core';
import { User } from "../models/user.model";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import {ImageService} from "../services/image.service";
import {UserService} from "../services/user.service";
import {ToastrService} from "ngx-toastr";
import {UserDTO} from "../models/profile.model";
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-profile-information',
  templateUrl: './profile-information.component.html',
  styleUrls: ['./profile-information.component.css'] // styleUrl is not a valid property
})
export class ProfileInformationComponent implements OnInit {
  @Input() user!: User;
  userFormUpdate!: FormGroup;
  userPictureForm!: FormGroup;
  imageFile!: File;
  safeImageUrl!: string | null;


  constructor(private userService: UserService, private imageService: ImageService, private toastr: ToastrService,  private sanitizer: DomSanitizer, private authService: AuthenticationService) { }

  ngOnInit(): void {
    console.log(this.user);
    this.userFormUpdate = new FormGroup({
      email: new FormControl(this.user.email, Validators.required),
      name: new FormControl(this.user.name),
      surname: new FormControl(this.user.surname),
      phoneNumber: new FormControl(this.user.phoneNumber),
      birthday: new FormControl(this.user.birthday),
      address: new FormControl(this.user.address),
      city: new FormControl(this.user.city),
      zipCode: new FormControl(this.user.zipCode),

    });

    this.userPictureForm = new FormGroup({
      photo: new FormControl(null)
    })

    this.loadPhoto();

  }





  onSubmit() {


    const userData = this.userFormUpdate.value;

    const user = new UserDTO(
      userData.email,
      userData.name,
      userData.surname,
      userData.phoneNumber,
      userData.birthday,
      userData.address,
      userData.city,
      userData.zipCode
    );

    console.log(user);
    console.log(JSON.stringify(user));



    this.userService.updateUser(this.user.id!, user).subscribe({
      next: (response) => {
        this.toastr.success("You have successfully updated your profile");
      },
      error: (error) => {
        this.toastr.error("There has been an error, try again later.");
      }
    });

    console.log(this.userFormUpdate.value);
  }

  savePhoto() {

    const formData = new FormData();
    formData.append('photo', this.imageFile);

    this.userService.updateUserPhoto(this.user.id!, formData).subscribe({
      next: () => {
        this.toastr.success("You have successfully uploaded the photo");
        this.loadPhoto();
        window.location.reload();
      },
      error: () => {
        this.toastr.error("There has been an error, try again later.");
      }
    })
  }

  loadPhoto(){


      this.imageService.getImage(this.user.image!.path).subscribe(
        response => {
          const url = window.URL.createObjectURL(response);
          this.safeImageUrl = <string>this.sanitizer.bypassSecurityTrustUrl(url);
        }, error => {
          console.error('Error fetching image:', error);
        });





  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.imageFile = input.files[0];

    }
  }
}
