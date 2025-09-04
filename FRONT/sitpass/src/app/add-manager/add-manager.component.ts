import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { User } from "../models/user.model";
import { Manager } from "../models/managers.model";
import { UserService } from "../services/user.service";
import { ToastrService } from "ngx-toastr";
import { Facility } from "../models/facility.model";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {UserDTO} from "../models/dto/user-dto.model";

@Component({
  selector: 'app-add-manager',
  templateUrl: './add-manager.component.html',
  styleUrls: ['./add-manager.component.css']
})
export class AddManagerComponent implements OnInit {
  managerForm!: FormGroup;
  users: User[] = [];
  currentManager: Manager | null = null;
  facility: Facility;

  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private dialogRef: MatDialogRef<AddManagerComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { facility: Facility }
  ) {
    this.facility = data.facility;
  }

  ngOnInit(): void {
    this.managerForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
    });

    this.loadUsers();
    this.loadCurrentManager();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (response) => {
        if (response) {
          console.log(response);
          this.users = response;
        }
        else {
          this.users = [];
        }

      },
      error: (error) => {
        this.toastr.error("Failed to load users.");
      }
    });
  }

  loadCurrentManager(): void {
    console.log("Current Manager: " + this.facility.id);
    this.userService.getManager(this.facility.id!).subscribe({
      next: (response) => {
        if (response){
          console.log(response);
          this.currentManager = response;
        }
        else {
          console.log("Error")
          this.currentManager = null;
        }

      },
      error: (error) => {
        this.toastr.error("Failed to load manager.");
      }
    });
  }

  setManager(): void {
    if (this.managerForm.valid && !this.currentManager) {
      const selectedEmail = this.managerForm.value.email;
      const selectedUser = this.users.find(user => user.email === selectedEmail);
      console.log(selectedUser);
      if (selectedUser) {
        let userDto = new UserDTO(selectedUser.email);
        const newManager: Manager = new Manager(userDto, new Date(), this.facility.id!)
        console.log(newManager);

        this.userService.setManager(newManager).subscribe({
          next: () => {
            this.toastr.success("Manager set successfully.");
            this.currentManager = newManager;
            this.managerForm.reset();
            this.dialogRef.close(true); // Close the dialog and pass success status
          },
          error: () => {
            this.toastr.error("Failed to set manager.");
          }
        });
      }
    }
  }

  removeManager(): void {
    if (this.currentManager) {
      this.userService.removeManager(this.currentManager).subscribe({
        next: () => {
          this.toastr.success("Manager removed successfully.");
          this.currentManager = null;
          this.dialogRef.close(true); // Close the dialog and pass success status
        },
        error: () => {
          this.toastr.error("Failed to remove manager.");
        }
      });
    }
  }
}
