export class Message {
    content: string;
    type: string;
    disabled: boolean;

    constructor(content?: string, type?: string, disabled?: boolean) {
      this.content = content;
      this.type = type;
      this.disabled = disabled;
    }
}
