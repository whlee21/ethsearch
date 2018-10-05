import { Directive, ElementRef, Renderer2, HostListener } from '@angular/core';

@Directive({
    // tslint:disable-next-line:directive-selector
    selector: '[myColor]'
})
export class TextColorDirective {
    @HostListener('click', ['$event'])
    elementClick(e) {
        alert(e.srcElement.innerHTML);
    }

    constructor(elementref: ElementRef, renderer: Renderer2) {
        renderer.setStyle(elementref.nativeElement, 'color', 'pupple');
    }
}
