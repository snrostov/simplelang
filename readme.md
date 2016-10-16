Simple programming language for learning compiler theory

-[x] [Model](src/main/kotlin/org/srostov/simplelang/Model.kt) - Point to start
-[x] [Visitor]() - Main logic
    -[x] [Eval]() - Evaluate program
    -[x] [Print]() - Print program
    -[ ] [Optimize]() - Basic optimizations
        -[x] [Const]() - Constant fold & propogation ( `2 + 2 => 2`; `if (true) x else y => x`; `f(const, const) => eval f`)        
        -[ ] [Ops]() - Simple base operations optimization ( `a * 0 => 0`; `a * 1 => 1`; ... )
        -[ ] [CSE]() - Common subexpression elimination
        -[ ] [If]() - Eliminate known expression checks
        -[x] [Inline]() - Inline function for further processing ( `f(x, y) = x + x; f(2, 0)` => `2 + 0` => `2` )
        -[ ] [TailRecursion]() - Recursive tail call => loop
        -[ ] [Loop]() - Simple loop optimizations 
          -[ ] [LoopCollapse]()
          -[ ] [LoopFusion]()
          -[ ] [LoopHoisting]()
          -[ ] [LoopInvariant]()
          -[ ] [LoopUnroll]()
          -[ ] [LoopUnswitch]()
    -[ ] [Emit]() - Machine code generation
        -[ ] [RegAlloc]() - Register allocation
        -[ ] [x86_64]() - Transform operations to x86_64 instructions