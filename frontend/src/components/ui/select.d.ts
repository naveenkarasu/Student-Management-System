import * as React from 'react';
export interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
    options: {
        value: string;
        label: string;
    }[];
    placeholder?: string;
}
declare const Select: React.ForwardRefExoticComponent<SelectProps & React.RefAttributes<HTMLSelectElement>>;
export { Select };
