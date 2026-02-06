import { useRef, useState } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import type { Grade } from '@/types';
import { getGradeColor } from '@/types';
import * as THREE from 'three';

interface AcademicOrbitProps {
  grades: Grade[];
}

function CentralSphere() {
  const meshRef = useRef<THREE.Mesh>(null);

  useFrame((_, delta) => {
    if (meshRef.current) {
      meshRef.current.rotation.y += delta * 0.3;
    }
  });

  return (
    <mesh ref={meshRef}>
      <sphereGeometry args={[0.6, 32, 32]} />
      <meshStandardMaterial color="#6366f1" metalness={0.3} roughness={0.4} />
    </mesh>
  );
}

function CourseSphere({
  grade,
  index,
  total,
}: {
  grade: Grade;
  index: number;
  total: number;
}) {
  const groupRef = useRef<THREE.Group>(null);
  const [hovered, setHovered] = useState(false);
  const color = getGradeColor(grade.letterGrade);
  const size = Math.max(0.15, (grade.gradePoints / 4.0) * 0.4);
  const orbitRadius = 1.5 + grade.credits * 0.4;
  const angle = (index / total) * Math.PI * 2;
  const speed = 0.2 + index * 0.05;

  useFrame((state) => {
    if (groupRef.current) {
      const t = state.clock.elapsedTime * speed + angle;
      groupRef.current.position.x = Math.cos(t) * orbitRadius;
      groupRef.current.position.z = Math.sin(t) * orbitRadius;
      groupRef.current.position.y = Math.sin(t * 0.5) * 0.3;
    }
  });

  return (
    <group ref={groupRef}>
      <mesh
        onPointerOver={() => setHovered(true)}
        onPointerOut={() => setHovered(false)}
        scale={hovered ? 1.3 : 1}
      >
        <sphereGeometry args={[size, 16, 16]} />
        <meshStandardMaterial color={color} metalness={0.2} roughness={0.5} />
      </mesh>
      {hovered && (
        <Html distanceFactor={8} style={{ pointerEvents: 'none' }}>
          <div className="bg-white border border-slate-200 shadow-lg rounded-md px-3 py-2 text-xs whitespace-nowrap">
            <p className="font-semibold text-slate-900">{grade.courseName}</p>
            <p className="text-slate-600">
              Grade: {grade.letterGrade} ({grade.marks}%)
            </p>
            <p className="text-slate-600">Credits: {grade.credits}</p>
          </div>
        </Html>
      )}
    </group>
  );
}

function OrbitRings({ grades }: { grades: Grade[] }) {
  const uniqueRadii = [...new Set(grades.map((g) => 1.5 + g.credits * 0.4))];
  return (
    <>
      {uniqueRadii.map((radius) => (
        <mesh key={radius} rotation={[-Math.PI / 2, 0, 0]}>
          <ringGeometry args={[radius - 0.01, radius + 0.01, 64]} />
          <meshBasicMaterial color="#e5e7eb" side={THREE.DoubleSide} transparent opacity={0.5} />
        </mesh>
      ))}
    </>
  );
}

function Scene({ grades }: { grades: Grade[] }) {
  return (
    <>
      <ambientLight intensity={0.5} />
      <directionalLight position={[5, 5, 5]} intensity={0.7} />
      <pointLight position={[0, 0, 0]} intensity={0.5} color="#6366f1" />
      <CentralSphere />
      <OrbitRings grades={grades} />
      {grades.map((grade, index) => (
        <CourseSphere key={grade.id} grade={grade} index={index} total={grades.length} />
      ))}
      <OrbitControls enablePan enableZoom enableRotate minDistance={3} maxDistance={15} />
    </>
  );
}

export function AcademicOrbit({ grades }: AcademicOrbitProps) {
  if (grades.length === 0) {
    return (
      <div className="flex items-center justify-center h-64 text-slate-500">
        No grade data available for orbit visualization.
      </div>
    );
  }

  return (
    <div className="w-full h-80 rounded-lg border border-slate-200 bg-white">
      <Canvas camera={{ position: [0, 4, 6], fov: 50 }}>
        <Scene grades={grades} />
      </Canvas>
    </div>
  );
}
