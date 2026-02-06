import React, { createContext, useContext, useState, useCallback } from 'react';
import { cn } from '@/lib/utils';
import { X, CheckCircle, AlertCircle, Info } from 'lucide-react';

interface Toast {
  id: string;
  title: string;
  description?: string;
  variant?: 'default' | 'success' | 'destructive' | 'info';
}

interface ToastContextType {
  toasts: Toast[];
  addToast: (toast: Omit<Toast, 'id'>) => void;
  removeToast: (id: string) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [toasts, setToasts] = useState<Toast[]>([]);

  const addToast = useCallback((toast: Omit<Toast, 'id'>) => {
    const id = Math.random().toString(36).slice(2);
    setToasts((prev) => [...prev, { ...toast, id }]);
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, 4000);
  }, []);

  const removeToast = useCallback((id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  return (
    <ToastContext.Provider value={{ toasts, addToast, removeToast }}>
      {children}
      <div className="fixed bottom-4 right-4 z-[100] flex flex-col gap-2 w-96">
        {toasts.map((toast) => (
          <ToastItem key={toast.id} toast={toast} onRemove={removeToast} />
        ))}
      </div>
    </ToastContext.Provider>
  );
}

function ToastItem({ toast, onRemove }: { toast: Toast; onRemove: (id: string) => void }) {
  const Icon = toast.variant === 'success' ? CheckCircle
    : toast.variant === 'destructive' ? AlertCircle
    : Info;

  return (
    <div
      className={cn(
        'flex items-start gap-3 rounded-lg border p-4 shadow-lg transition-all bg-white',
        toast.variant === 'destructive' && 'border-red-200 bg-red-50',
        toast.variant === 'success' && 'border-green-200 bg-green-50',
      )}
    >
      <Icon className={cn(
        'h-5 w-5 shrink-0 mt-0.5',
        toast.variant === 'success' && 'text-green-600',
        toast.variant === 'destructive' && 'text-red-600',
        (!toast.variant || toast.variant === 'default' || toast.variant === 'info') && 'text-primary-600',
      )} />
      <div className="flex-1">
        <p className="text-sm font-semibold">{toast.title}</p>
        {toast.description && <p className="text-sm text-slate-500 mt-1">{toast.description}</p>}
      </div>
      <button onClick={() => onRemove(toast.id)} className="shrink-0">
        <X className="h-4 w-4 text-slate-400 hover:text-slate-600" />
      </button>
    </div>
  );
}

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
}
