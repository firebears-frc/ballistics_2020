# Power Cell Ballistics

## Acceleration of Gravity

Location      | `𝑔` (according to Wolfram Alpha)
--------------|---------------------------------
Minneapolis   | 9.82498 m/s²
Duluth        | 9.82816 m/s²
Detroit       | 9.82025 m/s²
Earth average | 9.80665 m/s²

## Simple Parabolic Trajectory

### Step 1: Select apex at inner port

Set apex `𝐇` to difference in elevation from shooter to inner port.

### Step 2: Calculate velocity for trajectory to apex

#### Maximum height

`𝐇 = 𝑣²⋅sin²𝜃 / 2⋅𝑔`

__or__

```
     _______________
𝑣 = √ 2⋅𝑔⋅𝐇 / sin²𝜃
```

### Step 3: Calculate time to outer port

Set `𝑥` as range to outer port.

#### X Displacement

`𝑥 = 𝑣⋅𝑡⋅cos𝜃`

__or__

`𝑡 = 𝑥 / 𝑣⋅cos𝜃`

### Step 4: Calculate elevation at outer port

#### Y Displacement

`𝑦 = 𝑣⋅𝑡⋅sin𝜃 - ½⋅𝑔⋅𝑡²`

### Step 5: Check outer port clearance

Compare power cell elevation and radius with outer port.  If there is enough
clearance, fire away!

### Step 6: Start over

Repeat steps 2-5, but use an apex between previous and top of outer port.

## Drag Force

`𝐷 = ½𝑝⋅𝑣²⋅𝐴⋅𝑐`

Where:
- 𝐷 is the drag force (kg⋅m/s² = N) in opposite direction of velocity
- 𝑝 is the fluid mass density (kg/m³)
  * For air at sea level 15°C, 𝑝 = 1.225 kg/m³
  * For air at 20°C, 1016 hPa, dew point 10°C, 𝑝 = 1.2019 kg/m³
- 𝑣 is projectile velocity (m/s)
- 𝐴 is the reference area (m²)
  * For a sphere, use cross-sectional area, 𝐴 = 𝜋⋅𝑟²
  * For power cell, 𝑟 = 0.0889 m (3.5 in), so 𝐴 = 0.024828666 m²
- 𝑐 is the drag coefficient (unitless), which varies by velocity
  * Experimentally determined, related to Reynolds number

### Reynolds Number

`𝑅 = 𝑣⋅𝑝⋅𝑙 / 𝜇`

Where:
- 𝑅 is the Reynolds number (unitless)
- 𝑙 is the reference length (m)
  * For a sphere, it is the diameter
  * For power cell, 𝑙 = 0.1778 m (7 in)
- 𝜇 is the viscosity coefficient (kg/m⋅s = Pa⋅s)
  * For air at 15°C, it is 1.81 x 10⁻⁵ Pa⋅s
  * For air at 25°C, it is 1.85 x 10⁻⁵ Pa⋅s

### Magnus Effect (Backspin)

`𝑀 = ½𝑝⋅𝓿²⋅𝐴⋅𝐿⋅(𝑤̂ ⨯ 𝑣̂)`

Where:
- 𝑀 is the Magnus force (N)
- 𝐿 is the lift coefficient, dependant on spin factor
  * Experimentally determined, 0.22 for a baseball
- 𝑤̂ is the angular velocity vector

#### Spin Factor

`𝑆 = 𝑤 / 𝑣`

Where:
- 𝑆 is the spin factor
- 𝑤 is the tangential velocity (m/s)
- 𝑣 is the translational velocity (m/s)
