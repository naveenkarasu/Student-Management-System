import { Canvas } from '@react-three/fiber';
import { OrbitControls, Text } from '@react-three/drei';
import type { Grade } from '@/types';
import { getGradeColor } from '@/types';

interface GradeChart3DProps {
  grades: Grade[];
}

function GradeBar({ grade, index, total }: { grade: Grade; index: number; total: number }) {
  const height = (grade.marks / 100) * 4;
  const color = getGradeColor(grade.letterGrade);
  const spacing = 1.5;
  const xPos = (index - (total - 1) / 2) * spacing;

  return (
    <group position={[xPos, 0, 0]}>
      <mesh position={[0, height / 2, 0]}>
        <cylinderGeometry args={[0.4, 0.4, height, 16]} />
        <meshStandardMaterial color={color} />
      </mesh>
      <Text
        position={[0, -0.5, 0]}
        rotation={[-Math.PI / 2, 0, 0]}
        fontSize={0.25}
        color="#374151"
        anchorX="center"
        anchorY="middle"
      >
        {grade.courseCode}
      </Text>
      <Text
        position={[0, height + 0.4, 0]}
        fontSize={0.25}
        color="#374151"
        anchorX="center"
        anchorY="middle"
      >
        {`${grade.marks} (${grade.letterGrade})`}
      </Text>
    </group>
  );
}

function GroundPlane() {
  return (
    <mesh rotation={[-Math.PI / 2, 0, 0]} position={[0, -0.01, 0]} receiveShadow>
      <planeGeometry args={[20, 10]} />
      <meshStandardMaterial color="#f3f4f6" />
    </mesh>
  );
}

function Scene({ grades }: { grades: Grade[] }) {
  return (
    <>
      <ambientLight intensity={0.6} />
      <directionalLight position={[5, 8, 5]} intensity={0.8} />
      <GroundPlane />
      {grades.map((grade, index) => (
        <GradeBar key={grade.id} grade={grade} index={index} total={grades.length} />
      ))}
      <OrbitControls
        enablePan
        enableZoom
        enableRotate
        minDistance={3}
        maxDistance={20}
      />
    </>
  );
}

export function GradeChart3D({ grades }: GradeChart3DProps) {
  if (grades.length === 0) {
    return (
      <div className="flex items-center justify-center h-64 text-slate-500">
        No grade data available for 3D chart.
      </div>
    );
  }

  return (
    <div className="w-full h-80 rounded-lg border border-slate-200 bg-white">
      <Canvas camera={{ position: [0, 5, 8], fov: 50 }}>
        <Scene grades={grades} />
      </Canvas>
    </div>
  );
}
