export interface IPost {
    id?: number;
    title?: string;
    content?: string;
    ownerId?: string;
    ownerFirstName?: string;
    ownerLastName?: string;
    ownerAccount?: string;
}

export class Post implements IPost {
    constructor(
        public id?: number,
        public title?: string,
        public content?: string,
        public ownerId?: string,
        public ownerFirstName?: string,
        public ownerLastName?: string,
        public ownerAccount?: string
    ) {}
}
