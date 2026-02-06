import { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';
import { authApi } from '@/api/auth';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '@/components/ui/card';
import { GraduationCap, LogIn } from 'lucide-react';
import { FloatingShapes } from '@/components/three/FloatingShapes';

const demoAccounts = [
  { username: 'admin', password: 'admin123', role: 'ADMIN' as const, description: 'Full access to all features' },
  { username: 'faculty1', password: 'faculty123', role: 'FACULTY' as const, description: 'Manage courses and grade entry' },
  { username: 'student1', password: 'student123', role: 'STUDENT' as const, description: 'View grades and transcript' },
];

export function LoginPage() {
  const { isAuthenticated, login } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    try {
      const response = await authApi.login({ username, password });
      login({
        id: response.userId,
        username: response.username,
        role: response.role,
        token: response.token,
      });
      navigate('/dashboard');
    } catch {
      setError('Invalid username or password.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDemoLogin = (account: typeof demoAccounts[number]) => {
    setUsername(account.username);
    setPassword(account.password);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#faf5ff] px-4 relative overflow-hidden">
      <FloatingShapes />
      <div className="w-full max-w-md space-y-6">
        <div className="text-center">
          <GraduationCap className="mx-auto h-12 w-12 text-primary-600" />
          <h1 className="mt-4 text-3xl font-bold text-slate-900">Student Management System</h1>
          <p className="mt-2 text-sm text-slate-600">Sign in to your account</p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle className="text-xl">Login</CardTitle>
            <CardDescription>Enter your credentials to access the system.</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-4">
              {error && (
                <div className="rounded-md bg-red-50 border border-red-200 p-3 text-sm text-red-700">
                  {error}
                </div>
              )}
              <div className="space-y-2">
                <Label htmlFor="username">Username</Label>
                <Input
                  id="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Enter your username"
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Enter your password"
                  required
                />
              </div>
              <Button type="submit" className="w-full" disabled={isLoading}>
                <LogIn className="h-4 w-4 mr-2" />
                {isLoading ? 'Signing in...' : 'Sign In'}
              </Button>
            </form>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Demo Accounts</CardTitle>
            <CardDescription>Click to auto-fill credentials, then sign in.</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              {demoAccounts.map((account) => (
                <button
                  key={account.username}
                  onClick={() => handleDemoLogin(account)}
                  className="flex w-full items-center justify-between rounded-lg border border-slate-200 p-3 text-left transition-colors hover:bg-purple-50"
                >
                  <div>
                    <p className="text-sm font-medium text-slate-900">
                      {account.username}{' '}
                      <span className="text-xs text-slate-500">/ {account.password}</span>
                    </p>
                    <p className="text-xs text-slate-500">{account.description}</p>
                  </div>
                  <span className="text-xs font-semibold rounded-full bg-primary-100 text-primary-700 px-2 py-0.5">
                    {account.role}
                  </span>
                </button>
              ))}
            </div>
          </CardContent>
          <CardFooter />
        </Card>
      </div>
    </div>
  );
}
