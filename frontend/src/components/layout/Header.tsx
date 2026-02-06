import { useAuth } from '@/hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { LogOut, GraduationCap, Menu } from 'lucide-react';

interface HeaderProps {
  onToggleSidebar: () => void;
}

export function Header({ onToggleSidebar }: HeaderProps) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const roleBadgeVariant = user?.role === 'ADMIN'
    ? 'destructive' as const
    : user?.role === 'FACULTY'
    ? 'default' as const
    : 'success' as const;

  return (
    <header className="sticky top-0 z-40 flex h-16 items-center gap-4 border-b border-gray-200 bg-white px-4 lg:px-6">
      <button
        onClick={onToggleSidebar}
        className="lg:hidden rounded-md p-2 hover:bg-gray-100"
      >
        <Menu className="h-5 w-5" />
      </button>

      <div className="flex items-center gap-2">
        <GraduationCap className="h-6 w-6 text-primary-600" />
        <span className="text-lg font-semibold text-gray-900 hidden sm:inline">
          Student Management System
        </span>
        <span className="text-lg font-semibold text-gray-900 sm:hidden">
          SMS
        </span>
      </div>

      <div className="ml-auto flex items-center gap-4">
        {user && (
          <>
            <div className="hidden sm:flex items-center gap-2">
              <span className="text-sm text-gray-600">
                {user.username}
              </span>
              <Badge variant={roleBadgeVariant}>
                {user.role}
              </Badge>
            </div>
            <Button variant="ghost" size="sm" onClick={handleLogout}>
              <LogOut className="h-4 w-4 mr-2" />
              Logout
            </Button>
          </>
        )}
      </div>
    </header>
  );
}
