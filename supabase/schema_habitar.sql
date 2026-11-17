-- ==============================================================================
-- HabiTAR - Esquema de Base de Datos y Seguridad RLS (Supabase)
-- Proyecto: Módulo de Autoconocimiento y Screening TEA Adultos (Grupo 5 - GPI / UXLab)
-- Cumplimiento: Ley N° 21.719 de Protección de Datos Personales (Privacidad por diseño)
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. TABLA: perfil_usuario
-- Almacena los datos básicos del perfil de usuario vinculado a Supabase Auth.
-- ------------------------------------------------------------------------------
create table if not exists public.perfil_usuario (
    id_usuario uuid primary key references auth.users(id) on delete cascade,
    nombre_usuario text not null,
    correo_electronico text not null,
    edad integer,
    contrasenia text,
    carrera text,
    telefono text,
    created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- Habilitar Row Level Security (RLS)
alter table public.perfil_usuario enable row level security;

-- Políticas de seguridad RLS para perfil_usuario
create policy "Permitir a usuarios insertar su propio perfil"
    on public.perfil_usuario for insert
    with check (auth.uid() = id_usuario);

create policy "Permitir a usuarios ver su propio perfil"
    on public.perfil_usuario for select
    using (auth.uid() = id_usuario);

create policy "Permitir a usuarios actualizar su propio perfil"
    on public.perfil_usuario for update
    using (auth.uid() = id_usuario);


-- ------------------------------------------------------------------------------
-- 2. TABLA: screening_resultados
-- Almacena los resultados deterministas de los instrumentos CAT-Q y RBQ-2A
-- procesados para el usuario adulto.
-- ------------------------------------------------------------------------------
create table if not exists public.screening_resultados (
    id uuid primary key default gen_random_uuid(),
    id_usuario uuid references auth.users(id) on delete cascade,
    catq_total integer,
    catq_compensacion integer,
    catq_enmascaramiento integer,
    catq_asimilacion integer,
    rbq2a_media numeric(4,2),
    rbq2a_motor numeric(4,2),
    rbq2a_rutinas numeric(4,2),
    resumen_orientacion text,
    created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- Habilitar Row Level Security (RLS)
alter table public.screening_resultados enable row level security;

-- Políticas de seguridad RLS para screening_resultados
create policy "Permitir a usuarios insertar sus resultados de screening"
    on public.screening_resultados for insert
    with check (auth.uid() = id_usuario);

create policy "Permitir a usuarios ver sus propios resultados de screening"
    on public.screening_resultados for select
    using (auth.uid() = id_usuario);

create policy "Permitir a usuarios eliminar sus propios resultados"
    on public.screening_resultados for delete
    using (auth.uid() = id_usuario);
