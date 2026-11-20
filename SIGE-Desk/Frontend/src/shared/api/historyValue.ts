export function formatHistoryValue(value: string | null | undefined, emptyLabel: string): string {
  if (value == null) return emptyLabel;
  try {
    const parsed: unknown = JSON.parse(value);
    if (parsed == null) return emptyLabel;
    return typeof parsed === 'string' ? parsed : JSON.stringify(parsed, null, 2);
  } catch {
    return value || emptyLabel;
  }
}
