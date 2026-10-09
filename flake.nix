# flake.nix
{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };
  outputs = {nixpkgs, ...}: let
    inherit (nixpkgs) lib;
    withSystem = f:
      lib.foldr lib.recursiveUpdate {}
      (map f ["x86_64-linux" "x86_64-darwin" "aarch64-linux" "aarch64-darwin"]);
  in
    withSystem (
      system: let
        pkgs = nixpkgs.legacyPackages.${system};
      in {
        devShells.${system}.default =
          pkgs.mkShell
          {
            packages = with pkgs; [
              jdk21
            ];
            shellHook = ''
              export JAVA_HOME=${pkgs.jdk21.home}
            '';
          };
      }
    );
}
