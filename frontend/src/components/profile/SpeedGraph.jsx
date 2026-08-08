import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
} from 'recharts';

export default function SpeedGraph({ data }) {
  if (data.length === 0) {
    return <p className="empty-text">Complete a test to see your progress here.</p>;
  }

  const chartData = data.map((r, i) => ({
    index: i + 1,
    wpm: r.wpm,
    accuracy: r.accuracy,
  }));

  return (
    <div className="graph-wrap">
      <ResponsiveContainer width="100%" height={260}>
        <LineChart data={chartData} margin={{ top: 10, right: 12, left: -14, bottom: 4 }}>
          <CartesianGrid stroke="#EFE9FA" vertical={false} />
          <XAxis
            dataKey="index"
            tick={{ fontSize: 11, fill: '#8B84A0' }}
            tickLine={false}
            axisLine={{ stroke: '#EFE9FA' }}
            label={{ value: 'Test #', position: 'insideBottom', offset: -2, fontSize: 11, fill: '#8B84A0' }}
          />
          <YAxis tick={{ fontSize: 11, fill: '#8B84A0' }} tickLine={false} axisLine={false} width={34} />
          <Tooltip
            contentStyle={{
              borderRadius: 12,
              border: 'none',
              boxShadow: '0 4px 20px rgba(61,52,80,0.15)',
              fontFamily: 'Figtree, sans-serif',
              fontSize: 13,
            }}
          />
          <Legend wrapperStyle={{ fontSize: 12, fontFamily: 'Figtree, sans-serif' }} />
          <Line type="monotone" dataKey="wpm" name="WPM" stroke="#FF6B90" strokeWidth={3} dot={{ r: 3 }} activeDot={{ r: 5 }} />
          <Line
            type="monotone"
            dataKey="accuracy"
            name="Accuracy %"
            stroke="#3FC48D"
            strokeWidth={2.5}
            dot={{ r: 2.5 }}
            strokeDasharray="4 3"
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}
