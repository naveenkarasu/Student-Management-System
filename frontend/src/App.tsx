import type { ReactNode } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';
import { Layout } from '@/components/layout/Layout';
import { LoginPage } from '@/pages/LoginPage';
import { DashboardPage } from '@/pages/DashboardPage';
import { StudentsPage } from '@/pages/StudentsPage';
import { CoursesPage } from '@/pages/CoursesPage';
import { EnrollmentsPage } from '@/pages/EnrollmentsPage';
import { GradeEntryPage } from '@/pages/GradeEntryPage';
import { MyGradesPage } from '@/pages/MyGradesPage';
import { TranscriptPage } from '@/pages/TranscriptPage';
import type { Role } from '@/types';

function RoleGuard({ children, allowed }: { children: ReactNode; allowed: Role[] }) {
  const { user } = useAuth();
  if (!user || !allowed.includes(user.role)) {
    return <Navigate to="/dashboard" replace />;
  }
  return <>{children}</>;
}

function RootRedirect() {
  const { isAuthenticated, user } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  if (user?.role === 'STUDENT') {
    return <Navigate to="/my-grades" replace />;
  }
  if (user?.role === 'FACULTY') {
    return <Navigate to="/courses" replace />;
  }
  return <Navigate to="/dashboard" replace />;
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<Layout />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route
          path="/students"
          element={
            <RoleGuard allowed={['ADMIN']}>
              <StudentsPage />
            </RoleGuard>
          }
        />
        <Route
          path="/courses"
          element={
            <RoleGuard allowed={['ADMIN', 'FACULTY']}>
              <CoursesPage />
            </RoleGuard>
          }
        />
        <Route
          path="/enrollments"
          element={
            <RoleGuard allowed={['ADMIN']}>
              <EnrollmentsPage />
            </RoleGuard>
          }
        />
        <Route
          path="/grade-entry"
          element={
            <RoleGuard allowed={['FACULTY']}>
              <GradeEntryPage />
            </RoleGuard>
          }
        />
        <Route
          path="/my-grades"
          element={
            <RoleGuard allowed={['STUDENT']}>
              <MyGradesPage />
            </RoleGuard>
          }
        />
        <Route
          path="/transcript"
          element={
            <RoleGuard allowed={['STUDENT']}>
              <TranscriptPage />
            </RoleGuard>
          }
        />
      </Route>
      <Route path="/" element={<RootRedirect />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
