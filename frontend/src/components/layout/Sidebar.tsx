import { NavLink } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';
import { cn } from '@/lib/utils';
import {
  LayoutDashboard,
  Users,
  BookOpen,
  ClipboardList,
  GraduationCap,
  BarChart3,
  FileText,
  X,
} from 'lucide-react';
import type { Role } from '@/types';

interface SidebarProps {
  open: boolean;
  onClose: () => void;
}

interface NavItem {
  label: string;
  path: string;
  icon: React.ReactNode;
  roles: Role[];
}

const navItems: NavItem[] = [
  {
    label: 'Dashboard',
    path: '/dashboard',
    icon: <LayoutDashboard className="h-5 w-5" />,
    roles: ['ADMIN', 'FACULTY', 'STUDENT'],
  },
  {
    label: 'Students',
    path: '/students',
    icon: <Users className="h-5 w-5" />,
    roles: ['ADMIN'],
  },
  {
    label: 'Courses',
    path: '/courses',
    icon: <BookOpen className="h-5 w-5" />,
    roles: ['ADMIN', 'FACULTY'],
  },
  {
    label: 'Enrollments',
    path: '/enrollments',
    icon: <ClipboardList className="h-5 w-5" />,
    roles: ['ADMIN'],
  },
  {
    label: 'Grade Entry',
    path: '/grade-entry',
    icon: <GraduationCap className="h-5 w-5" />,
    roles: ['FACULTY'],
  },
  {
    label: 'My Grades',
    path: '/my-grades',
    icon: <BarChart3 className="h-5 w-5" />,
    roles: ['STUDENT'],
  },
  {
    label: 'Transcript',
    path: '/transcript',
    icon: <FileText className="h-5 w-5" />,
    roles: ['STUDENT'],
  },
];

export function Sidebar({ open, onClose }: SidebarProps) {
  const { user } = useAuth();

  const filteredItems = navItems.filter(
    (item) => user && item.roles.includes(user.role)
  );

  return (
    <>
      {/* Mobile overlay */}
      {open && (
        <div
          className="fixed inset-0 z-40 bg-black/50 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <aside
        className={cn(
          'fixed inset-y-0 left-0 z-50 w-64 bg-white border-r border-gray-200 transform transition-transform duration-200 ease-in-out lg:translate-x-0 lg:static lg:z-auto',
          open ? 'translate-x-0' : '-translate-x-full'
        )}
      >
        <div className="flex h-16 items-center justify-between px-4 border-b border-gray-200 lg:hidden">
          <span className="font-semibold text-gray-900">Menu</span>
          <button onClick={onClose} className="rounded-md p-2 hover:bg-gray-100">
            <X className="h-5 w-5" />
          </button>
        </div>

        <nav className="flex flex-col gap-1 p-4">
          <div className="mb-4 px-3 py-2 rounded-lg bg-primary-50">
            <p className="text-xs font-medium text-primary-600 uppercase tracking-wider">
              Navigation
            </p>
          </div>

          {filteredItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              onClick={onClose}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors',
                  isActive
                    ? 'bg-primary-50 text-primary-700'
                    : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900'
                )
              }
            >
              {item.icon}
              {item.label}
            </NavLink>
          ))}
        </nav>

        <div className="absolute bottom-4 left-4 right-4">
          <div className="rounded-lg bg-gray-50 p-3">
            <p className="text-xs text-gray-500">Logged in as</p>
            <p className="text-sm font-medium text-gray-900">{user?.username}</p>
            <p className="text-xs text-primary-600">{user?.role}</p>
          </div>
        </div>
      </aside>
    </>
  );
}
