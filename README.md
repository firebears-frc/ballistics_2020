# Power Cell Ballistics

## Acceleration of Gravity

Location      | `𝑔` (according to Wolfram Alpha)
--------------|---------------------------------
Minneapolis   | 9.82498 m/s²
Duluth        | 9.82816 m/s²
Detroit       | 9.82025 m/s²
Earth average | 9.80665 m/s²

## Step 1: Select apex at inner port

Set apex `𝐇` to difference in elevation from shooter to inner port.

## Step 2: Calculate velocity for trajectory to apex

### Maximum height

`𝐇 = 𝑣²⋅sin²𝜃 / 2⋅𝑔`

__or__

```
     _______________
𝑣 = √ 2⋅𝑔⋅𝐇 / sin²𝜃
```

## Step 3: Calculate time to outer port

Set `𝑥` as range to outer port.

### X Displacement

`𝑥 = 𝑣⋅𝑡⋅cos𝜃`

__or__

`𝑡 = 𝑥 / 𝑣⋅cos𝜃`

## Step 4: Calculate elevation at outer port

### Y Displacement

`𝑦 = 𝑣⋅𝑡⋅sin𝜃 - ½𝑔⋅𝑡²`

## Step 5: Check outer port clearance

Compare power cell elevation and radius with outer port.  If there is enough
clearance, fire away!

## Step 6: Start over

Repeat steps 2-5, but use an apex between previous and top of outer port.
