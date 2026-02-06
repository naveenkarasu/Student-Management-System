import { useState } from 'react';
import { useAuth } from '@/hooks/useAuth';
import { transcriptsApi } from '@/api/transcripts';
import { useStudentGrades, useStudentGpa } from '@/hooks/useGrades';
import { Button } from '@/components/ui/button';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/components/ui/card';
import { Table, TableHeader, TableBody, TableHead, TableRow, TableCell } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { Loading } from '@/components/ui/loading';
import { useToast } from '@/components/ui/toast';
import { Download, FileText } from 'lucide-react';
import { TranscriptScroll3D } from '@/components/three/TranscriptScroll3D';
import { demoUsers } from '@/mocks/data';

export function TranscriptPage() {
  const { user } = useAuth();
  const { addToast } = useToast();
  const [downloading, setDownloading] = useState(false);

  const demoUser = demoUsers.find((u) => u.username === user?.username);
  const studentId = demoUser?.linkedId ?? 0;

  const { data: grades, isLoading: loadingGrades } = useStudentGrades(studentId);
  const { data: gpa, isLoading: loadingGpa } = useStudentGpa(studentId);

  const handleDownload = async () => {
    setDownloading(true);
    try {
      const blob = await transcriptsApi.downloadPdf(studentId);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `transcript_${studentId}.txt`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
      addToast({ title: 'Transcript downloaded successfully.', variant: 'success' });
    } catch {
      addToast({ title: 'Failed to download transcript.', variant: 'destructive' });
    } finally {
      setDownloading(false);
    }
  };

  if (loadingGrades || loadingGpa) return <Loading />;

  const semesters = grades ? [...new Set(grades.map((g) => g.semester))] : [];

  return (
    <div className="space-y-6">
      <TranscriptScroll3D />

      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-primary-950">Academic Transcript</h1>
          <p className="text-primary-600">View and download your official academic transcript.</p>
        </div>
        <Button onClick={handleDownload} disabled={downloading}>
          <Download className="h-4 w-4 mr-2" />
          {downloading ? 'Downloading...' : 'Download Transcript'}
        </Button>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center gap-3">
            <FileText className="h-8 w-8 text-primary-600" />
            <div>
              <CardTitle>Official Academic Transcript</CardTitle>
              <CardDescription>Student: {user?.username}</CardDescription>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-6">
            {semesters.map((semester) => {
              const semGrades = grades?.filter((g) => g.semester === semester) ?? [];
              const semCredits = semGrades.reduce((sum, g) => sum + g.credits, 0);
              const semGradePoints = semGrades.reduce(
                (sum, g) => sum + g.gradePoints * g.credits,
                0
              );
              const semGpa = semCredits > 0 ? semGradePoints / semCredits : 0;

              return (
                <div key={semester}>
                  <div className="flex items-center justify-between mb-2">
                    <h3 className="text-lg font-semibold text-primary-950">{semester}</h3>
                    <Badge variant="secondary">
                      Semester GPA: {semGpa.toFixed(2)}
                    </Badge>
                  </div>
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Course Code</TableHead>
                        <TableHead>Course Name</TableHead>
                        <TableHead>Credits</TableHead>
                        <TableHead>Marks</TableHead>
                        <TableHead>Grade</TableHead>
                        <TableHead>Grade Points</TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {semGrades.map((g) => (
                        <TableRow key={g.id}>
                          <TableCell className="font-medium">{g.courseCode}</TableCell>
                          <TableCell>{g.courseName}</TableCell>
                          <TableCell>{g.credits}</TableCell>
                          <TableCell>{g.marks}</TableCell>
                          <TableCell>
                            <Badge
                              variant={
                                g.letterGrade === 'A'
                                  ? 'success'
                                  : g.letterGrade === 'F'
                                  ? 'destructive'
                                  : 'secondary'
                              }
                            >
                              {g.letterGrade}
                            </Badge>
                          </TableCell>
                          <TableCell>{g.gradePoints.toFixed(1)}</TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </div>
              );
            })}

            {semesters.length === 0 && (
              <p className="text-center text-primary-500 py-8">
                No academic records available yet.
              </p>
            )}

            {gpa && semesters.length > 0 && (
              <div className="mt-6 rounded-lg bg-primary-50 p-4 border border-primary-100">
                <div className="grid grid-cols-3 gap-4 text-center">
                  <div>
                    <p className="text-sm text-primary-600">Cumulative GPA</p>
                    <p className="text-2xl font-bold text-primary-950">{gpa.gpa.toFixed(2)}</p>
                  </div>
                  <div>
                    <p className="text-sm text-primary-600">Total Credits</p>
                    <p className="text-2xl font-bold text-primary-950">{gpa.totalCredits}</p>
                  </div>
                  <div>
                    <p className="text-sm text-primary-600">Total Grade Points</p>
                    <p className="text-2xl font-bold text-primary-950">{gpa.totalGradePoints.toFixed(1)}</p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
