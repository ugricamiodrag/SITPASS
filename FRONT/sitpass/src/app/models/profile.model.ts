

export class UserDTO {
  email: string;
  name?: string;
  surname?: string;
  phoneNumber?: string;
  birthday?: Date;
  address?: string;
  city?: string;
  zipCode?: string;


  constructor(
    email: string,
    name?: string,
    surname?: string,
    phoneNumber?: string,
    birthday?: Date,
    address?: string,
    city?: string,
    zipCode?: string,

  ) {
    this.email = email;

    this.name = name;
    this.surname = surname;
    this.phoneNumber = phoneNumber;
    this.birthday = birthday;
    this.address = address;
    this.city = city;
    this.zipCode = zipCode;
  }
}
