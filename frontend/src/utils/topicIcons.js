import {
  Cpu, Leaf, Trophy, FlaskConical, Flame, BookOpen,
  Briefcase, Landmark, Sparkles, Star, Heart, Music,
} from 'lucide-react';

export const ICON_MAP = {
  cpu: Cpu,
  leaf: Leaf,
  trophy: Trophy,
  flask: FlaskConical,
  flame: Flame,
  book: BookOpen,
  briefcase: Briefcase,
  landmark: Landmark,
  sparkles: Sparkles,
  star: Star,
  heart: Heart,
  music: Music,
};

export function getTopicIcon(iconKey) {
  return ICON_MAP[iconKey] || Sparkles;
}
