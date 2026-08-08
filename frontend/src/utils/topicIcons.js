import {
  Cpu, Leaf, Trophy, FlaskConical, Flame, BookOpen,
  Briefcase, Landmark, Sparkles, Star, Heart, Music,
} from 'lucide-react';

// Must match AdminTopicService.VALID_ICONS on the backend exactly — this is the
// full set of icons an admin can choose from when creating/editing a topic.
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
