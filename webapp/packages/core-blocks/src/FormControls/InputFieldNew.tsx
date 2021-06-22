/*
 * CloudBeaver - Cloud Database Manager
 * Copyright (C) 2020-2021 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0.
 * you may not use this file except in compliance with the License.
 */

import { observer } from 'mobx-react-lite';
import { useCallback, useContext, useState } from 'react';
import styled, { use, css } from 'reshadow';

import { ComponentStyle, composes, useStyles } from '@cloudbeaver/core-theming';

import type { ILayoutSizeProps } from '../Containers/ILayoutSizeProps';
import { Icon } from '../Icons/Icon';
import { baseFormControlStylesNew } from './baseFormControlStylesNew';
import { FormContext } from './FormContext';
import { isControlPresented } from './isControlPresented';

const INPUT_FIELD_STYLES = composes(
  css`
    Icon {
      composes: theme-text-primary from global;
    }
`,
  css`
    field-label {
      display: block;
      composes: theme-typography--body1 from global;
      font-weight: 500;
    }
    field-label:not(:empty) {
      padding-bottom: 10px;
    }
    input-container {
      position: relative;
    }
    Icon {
      position: absolute;
      right: 8px;
      top: 50%;
      transform: translateY(-50%);
      width: 24px;
      height: 24px;
      cursor: pointer;
    }
    input[disabled] + Icon {
      cursor: auto;
      opacity: 0.8;
    }
    input:not(:only-child) {
      padding-right: 32px !important;
    }
`);

type BaseProps = Omit<React.InputHTMLAttributes<HTMLInputElement>, 'onChange' | 'name' | 'value'> & ILayoutSizeProps & {
  description?: string;
  mod?: 'surface';
  ref?: React.Ref<HTMLInputElement>;
  style?: ComponentStyle;
};

type ControlledProps = BaseProps & {
  name?: string;
  value?: string | number;
  mapState?: (value: string | number) => string | number;
  mapValue?: (value: string | number) => string | number;
  onChange?: (value: string | number, name?: string) => any;
  state?: never;
  autoHide?: never;
};

type ObjectProps<TKey extends keyof TState, TState> = BaseProps & {
  name: TKey;
  state: TState;
  mapState?: (value: TState[TKey]) => TState[TKey] | string | number;
  mapValue?: (value: TState[TKey]) => TState[TKey];
  onChange?: (value: TState[TKey], name: TKey) => any;
  autoHide?: boolean;
  value?: never;
};

interface InputFieldType {
  (props: ControlledProps): React.ReactElement<any, any> | null;
  <TKey extends keyof TState, TState>(props: ObjectProps<TKey, TState>): React.ReactElement<any, any> | null;
}

export const InputFieldNew: InputFieldType = observer(function InputFieldNew({
  name,
  style,
  value: valueControlled,
  defaultValue,
  required,
  state,
  mapState,
  mapValue,
  children,
  className,
  description,
  mod,
  small,
  medium,
  large,
  autoHide,
  onChange,
  ...rest
}: ControlledProps | ObjectProps<any, any>, ref: React.Ref<HTMLInputElement>) {
  const [passwordRevealed, setPasswordRevealed] = useState(false);
  const styles = useStyles(baseFormControlStylesNew, INPUT_FIELD_STYLES, style);
  const context = useContext(FormContext);

  const revealPassword = useCallback(() => {
    if (rest.disabled) {
      return;
    }

    setPasswordRevealed(prev => !prev);
  }, [rest.disabled]);

  const handleChange = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    const value = mapValue?.(event.target.value) ?? event.target.value;

    if (state) {
      state[name] = value;
    }
    if (onChange) {
      onChange(value, name);
    }
    if (context) {
      context.change(value, name);
    }
  }, [state, name, context, onChange]);

  if (autoHide && !isControlPresented(name, state, defaultValue)) {
    return null;
  }

  let value: any = valueControlled ?? defaultValue ?? undefined;

  if (state && name !== undefined && name in state) {
    value = state[name];
  }

  if (mapState) {
    value = mapState(value);
  }

  const showRevealPasswordButton = rest.type === 'password' && !rest.readOnly;

  return styled(styles)(
    <field className={className} {...use({ small, medium, large })}>
      <field-label title={rest.title}>{children}{required && ' *'}</field-label>
      <input-container>
        <input
          ref={ref}
          role='new'
          {...rest}
          type={passwordRevealed ? 'text' : rest.type}
          name={name}
          value={value ?? ''}
          onChange={handleChange}
          {...use({ mod })}
          required={required}
        />
        {showRevealPasswordButton && (
          <Icon
            name={passwordRevealed ? 'password-hide' : 'password-show'}
            viewBox='0 0 16 16'
            onClick={revealPassword}
          />
        )}
      </input-container>
      {description && (
        <field-description>
          {description}
        </field-description>
      )}
    </field>
  );
}, { forwardRef: true });
