

export class User {
    id?: number;
    email: string;
    password: string;
    name?: string;
    surname?: string;
    createdAt?: Date;
    phoneNumber?: string;
    birthday?: Date;
    address?: string;
    city?: string;
    zipCode?: string;
    image?: Image;

    constructor(
        email: string,
        password: string,
        name?: string,
        surname?: string,
        createdAt?: Date,
        phoneNumber?: string,
        birthday?: Date,
        address?: string,
        city?: string,
        zipCode?: string,
        image?: Image
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.createdAt = createdAt;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.image = image;
    }
}

export interface Image {
  id: number;
  path: string;
  user: User;
}
