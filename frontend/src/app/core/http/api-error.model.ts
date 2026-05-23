export interface ApiError {
  timestamp?: string;
  status: number;
  error?: string;
  message: string;
  path?: string;
  fields?: Record<string, string[]>;
  errors?: Record<string, string[]>;
  fieldErrors?: Record<string, string[]>;
}
